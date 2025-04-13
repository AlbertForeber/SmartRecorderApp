package com.example.smartrecorderapp.topbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.smartrecorderapp.database.ViewModelLDB
import com.example.smartrecorderapp.date_functional.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenTopBar(lessonLDB: ViewModelLDB) {
    var dateParams = formatDate( lessonLDB.selectedData.value )
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
            IconButton(onClick = { lessonLDB.isSelectingDate.value = true }) {
                Icon(Icons.Filled.DateRange, "Выбор даты", tint = MaterialTheme.colorScheme.primary)
            }
        }
    )
}