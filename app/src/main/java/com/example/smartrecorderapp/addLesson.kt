package com.example.smartrecorderapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlin.math.sin

@Composable
fun AddLesson(onDismissRequest: () -> Unit) {
    var lessonName by remember { mutableStateOf("") }
    var lessonIndex by remember { mutableStateOf("1") }
    var isDropped by remember { mutableStateOf(false) }
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card (
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier.padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Добавление занятия", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = lessonName,
                        onValueChange = { lessonName = it },
                        label = { Text("Название пары") },
                        singleLine = true,
                        modifier = Modifier.width(200.dp)
                    )
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "№ пары",
                            style = MaterialTheme.typography.labelMedium
                        )
                        Box {
                            TextButton(
                                onClick = { isDropped = !isDropped },
                            ) {
                                Text(
                                    lessonIndex,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            DropdownMenu(
                                expanded = isDropped,
                                onDismissRequest = { isDropped = !isDropped },
                            ) {
                                DropdownMenuItem(
                                    text = { Text("1-ая пара") },
                                    onClick = {
                                        lessonIndex = "1"
                                        isDropped = !isDropped
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("2-ая пара") },
                                    onClick = {
                                        lessonIndex = "2"
                                        isDropped = !isDropped
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("3-я пара") },
                                    onClick = {
                                        lessonIndex = "3"
                                        isDropped = !isDropped
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("4-ая пара") },
                                    onClick = {
                                        lessonIndex = "4"
                                        isDropped = !isDropped
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("5-ая пара") },
                                    onClick = {
                                        lessonIndex = "5"
                                        isDropped = !isDropped
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("6-ая пара") },
                                    onClick = {
                                        lessonIndex = "6"
                                        isDropped = !isDropped
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("7-ая пара") },
                                    onClick = {
                                        lessonIndex = "7"
                                        isDropped = !isDropped
                                    }
                                )
                            }
                        }
                    }
                }
                ElevatedButton(
                    onClick = {onDismissRequest()}
                ) {
                    Text("Подтвердить")
                }
            }
        }
    }
}