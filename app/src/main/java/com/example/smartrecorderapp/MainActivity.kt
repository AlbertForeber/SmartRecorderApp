package com.example.smartrecorderapp

import android.annotation.SuppressLint
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.smartrecorderapp.ui.theme.SmartRecorderAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class MainActivity : ComponentActivity() {
    private lateinit var lessonLDB: ViewModelLDB

    @SuppressLint("MutableCollectionMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartRecorderAppTheme {
                var isSelectingDate by remember { mutableStateOf(false) }
                var isDialog by remember { mutableStateOf(false) }
                var selectedDate by remember { mutableLongStateOf(System.currentTimeMillis()) }
                var dateParams = formatDate(selectedDate)
                val provider = ViewModelProvider(this)
                val coroutineScope = rememberCoroutineScope()
                //
                var lesson by remember { mutableStateOf(mutableListOf<Day>()) }
                //
                lessonLDB = provider[ViewModelLDB::class]
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
                                        dateParams[0],
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text("${dateParams[2]} · 7 неделя",
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
//                    LaunchedEffect(isDialog, isSelectingDate) {
//                        lessonLDB.getDay(dateParams[1].toInt(), lessons = { lesson = it.toMutableList() }
//                        )
//                    }
                    LaunchedEffect(Unit) {
                        lessonLDB.getDay(dateParams[1].toInt(), lessons = { lesson = it.toMutableList() } )
                    }


                    LazyColumn(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        items(count = lesson.size) { it ->
                            LessonCard(
                                lesson[it].lessons,
                                lessonId = lesson[it].lessonId,
                                innerPadding,
                                onDelete = {
                                    lessonLDB.deleteLessonById(lesson[it].id, lesson[it].lessonId)
                                    coroutineScope.launch {
                                        lessonLDB.getDay(dateParams[1].toInt(), lessons = { lesson = it.toMutableList() })
                                    }
                                }
                            )
                        }
                    }

                    if (isDialog) {
                        AddLesson(
                            {
                                isDialog = false
                                coroutineScope.launch {
                                    lessonLDB.getDay(dateParams[1].toInt(), lessons = { lesson = it.toMutableList() })
                                }
                            },
                            lessonLDB,
                            dateParams[1].toInt(),
                        )
                    }
                    if (isSelectingDate) {
                        SelectData(
                            onConfirm = { date ->
                                if (date != null) {
                                    selectedDate = date

                                    coroutineScope.launch {
                                        lessonLDB.getDay(
                                            formatDate(date)[1].toInt(),
                                            lessons = { lesson = it.toMutableList() })
                                    }
                                }
                            },
                            onDismiss = {
                                isSelectingDate = false
                            }
                        )
                    }
                }
            }
        }
    }
}