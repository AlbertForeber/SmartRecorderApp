package com.example.smartrecorderapp.topbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenTopBar(
    lessonLDB: ViewModelLDB,
    auth: FirebaseAuth,
    returnAuth: (FirebaseUser? ) -> Unit,
    navController: NavHostController
) {
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
        navigationIcon = {
            IconButton(
                onClick = {
                    auth.signOut()
                    returnAuth( auth.currentUser )
                    navController.navigate("auth") {
                        popUpTo("home") { inclusive = true }
                    }

                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Кнопка выхода из аккаунта",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            IconButton(onClick = { lessonLDB.isSelectingDate.value = true }) {
                Icon(Icons.Filled.DateRange, "Выбор даты", tint = MaterialTheme.colorScheme.primary)
            }
        }
    )
}