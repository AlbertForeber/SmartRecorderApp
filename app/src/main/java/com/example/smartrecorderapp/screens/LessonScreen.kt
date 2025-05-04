package com.example.smartrecorderapp.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.util.lruCache
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavHostController
import com.example.smartrecorderapp.MainActivity
import com.example.smartrecorderapp.R
import com.example.smartrecorderapp.audio_processing.AndroidAudioPlayer
import com.example.smartrecorderapp.audio_processing.AndroidAudioRecorder
import com.example.smartrecorderapp.database.ViewModelLDB
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    navController: NavHostController,
    lessonLDB: ViewModelLDB,
    innerPadding: PaddingValues,
    context: Context
) {
    val recorder by lazy { AndroidAudioRecorder(context) }
    val player by lazy { AndroidAudioPlayer(context) }
    var audioFile: File? = null

    var debugData = lessonLDB.getRememberedData()
    var isRecordAllowed by remember {
        mutableStateOf(checkPermissionFor(context, Manifest.permission.RECORD_AUDIO) )
    }

    var getAudioPermission = rememberLauncherForActivityResult( contract = ActivityResultContracts.RequestPermission() ) {
        isRecordAllowed = it
    }

    Column(
        Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        IconButton(
            onClick = {
                if (!isRecordAllowed) {
                    getAudioPermission.launch(Manifest.permission.RECORD_AUDIO)
                }
                else {
                    File(context.cacheDir, "audio.mp3").also {
                        recorder.start(it)
                        audioFile = it
                    }

                }
            },
        ) {
            Icon(
                painterResource( R.drawable.mic_icon ), "Кнопка записи",
                modifier = Modifier.requiredSize(50.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        IconButton(
            onClick = {
                recorder.stop()
            },
        ) {
            Icon(
                Icons.Filled.Close,
                "Кнопка остановки записи",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        IconButton(
            onClick = {
                player.playFile(audioFile!!)
            },
        ) {
            Icon(
                Icons.Filled.PlayArrow,
                "Кнопка воспроизве записи",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }

}

fun checkPermissionFor(activity: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
}
