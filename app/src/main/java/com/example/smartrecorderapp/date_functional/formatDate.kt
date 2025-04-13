package com.example.smartrecorderapp.date_functional

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun formatDate(milliDate: Long): List<String> {
    val dateParams = mutableListOf("", "", "")
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

    dateParams[1] = when (calendar.get(Calendar.DAY_OF_WEEK)) {
        1 -> "7"
        2 -> "1"
        3 -> "2"
        4 -> "3"
        5 -> "4"
        6 -> "5"
        7 -> "6"
        else -> ""
    }

    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    dateParams[2] = formatter.format(Date(milliDate))
    return dateParams
}