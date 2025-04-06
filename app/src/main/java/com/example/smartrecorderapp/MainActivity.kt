package com.example.smartrecorderapp

import android.content.res.Resources.Theme
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.smartrecorderapp.ui.theme.SmartRecorderAppTheme
import java.util.Calendar
import java.util.Date

class MainActivity : ComponentActivity() {
    private lateinit var lessonLDB: ViewModelLDB

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartRecorderAppTheme {
                var isSelectingDate by remember { mutableStateOf(false) }
                var isDialog by remember { mutableStateOf(false) }
                var selectedDate by remember { mutableLongStateOf(System.currentTimeMillis()) }
                val context = LocalContext.current
                val provider = ViewModelProvider(this)
                //
                var lesson by remember { mutableStateOf("") }
                //
                lessonLDB = provider[ViewModelLDB::class]
                val day = lessonLDB.getDays()?.observeAsState(initial = listOf())
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                titleContentColor = MaterialTheme.colorScheme.background,
                            ),
                            title = {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Text(
                                        formatDate(selectedDate)[0],
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text("${formatDate(selectedDate)[1]} · 7 неделя",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface)
                                }
                            },
                            actions = {
                                IconButton(onClick = { isSelectingDate = true }) {
                                    Icon(Icons.Filled.DateRange, "Выбор даты", tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.surface,
                            onClick = {
                                isDialog = true
                            }
                        ) {
                            Icon(Icons.Filled.Add, "Кнопка добавления занятия", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                ) { innerPadding ->

                    // Получение нового списка при добавлении дня
                    LaunchedEffect(isDialog) {
                        lessonLDB.getDay(1, lessons = { lesson = it }
                        )
                    }

                    var lessonList = lesson.split('/')
                    LazyColumn(modifier = Modifier.padding(innerPadding)) {
                        items(count = lessonList.size) {
                            Text(lessonList[it])
                        }
                    }

                    if (isDialog) {
                        AddLesson(
                            {
                                isDialog = false
                            },
                            lessonLDB
                        )
                    }
                    if (isSelectingDate) {
                        SelectData(
                            onConfirm = { date ->
                                if (date != null) {
                                    selectedDate = date
                                }
                            },
                            onDismiss = { isSelectingDate = false }
                        )
                    }
                }
            }
        }
    }
}