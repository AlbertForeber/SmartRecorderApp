package com.example.smartrecorderapp.screens

import android.annotation.SuppressLint
import android.widget.Space
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.smartrecorderapp.AddLesson
import com.example.smartrecorderapp.LessonCard
import com.example.smartrecorderapp.database.Day
import com.example.smartrecorderapp.database.ViewModelLDB
import com.example.smartrecorderapp.date_functional.SelectData
import com.example.smartrecorderapp.date_functional.formatDate
import kotlinx.coroutines.launch

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, lessonLDB: ViewModelLDB, innerPadding: PaddingValues) {
    var isSelectingDate by remember { lessonLDB.isSelectingDate }
    var isDialog by remember { lessonLDB.isDialog }
    var selectedDate by remember { lessonLDB.selectedData }
    var dateParams = formatDate(selectedDate)
    val coroutineScope = rememberCoroutineScope()
    var lesson by remember { mutableStateOf(mutableListOf<Day>()) }

    LaunchedEffect(Unit) {
        lessonLDB.getDay(dateParams[1].toInt(), lessons = { lesson = it.toMutableList() } )
        navController.currentDestination?.route
    }


    LazyColumn(
        modifier = Modifier.padding(innerPadding)
    ) {
        item {
            Spacer(Modifier.height(5.dp))
        }
        items(count = lesson.size) { it ->
            Box (modifier = Modifier.animateItem(
                fadeInSpec = tween(250),
                fadeOutSpec = tween(250),
                placementSpec = tween(250),
            )) {
                LessonCard(
                    lesson[it].lessons,
                    lessonId = lesson[it].lessonId,
                    innerPadding,
                    onClick = {
                        lessonLDB.setRememberedData(selectedDate, lesson[it].lessonId, lesson[it].lessons)
                        navController.navigate("lesson")
                    },
                    onDelete = {
                        lessonLDB.deleteLessonById(
                            lesson[it].id,
                            lesson[it].lessonId
                        )
                        coroutineScope.launch {
                            lessonLDB.getDay(
                                dateParams[1].toInt(),
                                lessons = { lesson = it.toMutableList() })
                        }
                    }
                )
            }
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