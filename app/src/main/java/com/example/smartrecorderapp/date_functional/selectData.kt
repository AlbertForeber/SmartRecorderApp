package com.example.smartrecorderapp.date_functional

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectData(
    onDismiss: () -> Unit,
    onConfirm: (Long?) -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            ElevatedButton(
                onClick = {
                    onConfirm(datePickerState.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text("Подтвердить")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}