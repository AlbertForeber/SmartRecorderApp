package com.example.smartrecorderapp.topbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.smartrecorderapp.database.ViewModelLDB
import com.example.smartrecorderapp.date_functional.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreenTopBar(navController: NavHostController, lessonLDB: ViewModelLDB) {
    var debugData = lessonLDB.getRememberedData()
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
                    debugData[2],
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "${formatDate(debugData[0].toLong())} · 7 неделя",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigate("home") } ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    "Возврат на главный экран",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}