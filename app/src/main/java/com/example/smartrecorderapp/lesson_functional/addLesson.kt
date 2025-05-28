package com.example.smartrecorderapp.lesson_functional

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastCbrt
import androidx.compose.ui.window.Dialog
import com.example.smartrecorderapp.viewmodels.ViewModelLDB

@Composable
fun AddLesson(onDismissRequest: () -> Unit, lessonsLDB: ViewModelLDB, dayId: Int) {
    var lessonName by remember { mutableStateOf("") }
    var lessonIndex by remember { mutableStateOf("1") }
    var isDropped by remember { mutableStateOf(false) }
    var isAllowed by remember { mutableStateOf(true) }
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card (
            shape = MaterialTheme.shapes.large,
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Добавление занятия",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = lessonName,
                        onValueChange = { lessonName = it },
                        label = { Text("Имя_Тип") },
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
                if ( !isAllowed ) {
                    Text( "Недопустимое название", fontStyle = MaterialTheme.typography.labelSmall.fontStyle )
                }
                ElevatedButton(
                    onClick = {
                        if ( lessonName.isNotBlank() && lessonName.count { it == '_' } == 1 ) {
                            isAllowed = true;
                            lessonsLDB.insertDay(dayId, lessonIndex.toInt(), lessonName)
                            onDismissRequest()
                        }
                        else {
                            isAllowed = false
                        }
                    }
                ) {
                    Text("Подтвердить")
                }

            }
        }
    }
}
