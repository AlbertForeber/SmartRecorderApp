package com.example.smartrecorderapp

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun formatDate(milliDate: Long): List<String> {
    val dateParams = mutableListOf("", "")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milliDate
    dateParams[0] = when (calendar.get(Calendar.DAY_OF_WEEK)) {
        1 -> "Воскресенье"
        2 -> "Понедельник"
        3 -> "Вторник"
        4 -> "Среда"
        5 -> "Четверг"
        6 -> "Пятница"
        7 -> "Суббота"
        else -> ""
    }

//    dateParams[1] = "${calendar.get(Calendar.DATE)}.${calendar.get(Calendar.MONTH) + 1}.${calendar.get(Calendar.YEAR)}"
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    dateParams[1] = formatter.format(Date(milliDate))
    return dateParams
}