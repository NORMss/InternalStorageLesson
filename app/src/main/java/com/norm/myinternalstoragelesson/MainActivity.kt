package com.norm.myinternalstoragelesson

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.norm.myinternalstoragelesson.ui.theme.MyInternalStorageLessonTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyInternalStorageLessonTheme {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                val textValue = remember {
                    mutableStateOf("")
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = textValue.value,
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp
                            )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                textValue.value = readFile(context)
                            }
                        }
                    ) {
                        Text(text = "Read")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            scope.launch {
                                saveFile(context)
                            }
                        }
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}

private suspend fun saveFile(context: Context) {
    val text = "Categories of storage locations\n" +
            "Android provides two types of physical storage locations:" +
            "internal storage and external storage." +
            "On most devices, internal storage is smaller" +
            "than external storage. However, internal storage" +
            "is always available on all devices, making" +
            "it a more reliable place to put data" +
            "on which your app depends."
    withContext(Dispatchers.IO) {
        context.openFileOutput("text.txt", Context.MODE_PRIVATE).use {
            it.write(text.toByteArray())
        }
    }
}

private suspend fun readFile(context: Context) = withContext(Dispatchers.IO) {
    try {
        context.openFileInput("text.txt").bufferedReader()
            .useLines { lines ->
                lines.fold("") { acc, s ->
                    "$acc\n$s"
                }
            }
    } catch (e: IOException) {
        e.printStackTrace()
        ""
    }
}