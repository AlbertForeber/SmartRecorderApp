package com.example.smartrecorderapp.lesson_functional


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LessonCard(lessonString: String,
               lessonId: Int,
               padding: PaddingValues,
               onClick: () -> Unit,
               onDelete: () -> Unit
) {
    val lessonInfo = lessonString.split("_").toMutableList()
    if (lessonInfo.size == 1) { lessonInfo.add("ЛК") }
    val lessonTime = when (lessonId) {
        1 -> "09:00 - 10:30"
        2 -> "10:40 - 12:10"
        3 -> "12:40 - 14:10"
        4 -> "14:20 - 15:50"
        5 -> "16:20 - 17:50"
        6 -> "18:00 - 19:30"
        7 -> "20:00 - 21:30"
        else -> ""
    }
    Box(
        modifier = Modifier.padding(10.dp)
    ) {
        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = MaterialTheme.colorScheme.surface,
                disabledContentColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick
        ) {
            Column(
                modifier = Modifier.padding(15.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row {
                    Text(lessonTime)
                    Card(
                        shape = MaterialTheme.shapes.extraSmall,
                        colors = CardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            disabledContainerColor = MaterialTheme.colorScheme.surface,
                            disabledContentColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .padding(15.dp, 0.dp)
                    ) {
                        Text(
                            lessonInfo[1],
                            modifier = Modifier
                                .padding(5.dp, 0.dp),
                            color = Color.White
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        lessonInfo[0],
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(
                        onClick = onDelete
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            "Кнопка удаления пары",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }

                }
            }

        }
    }
}