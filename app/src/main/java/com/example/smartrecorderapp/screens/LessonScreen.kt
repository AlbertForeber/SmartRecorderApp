package com.example.smartrecorderapp.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.smartrecorderapp.date_functional.StartDateReference
import com.example.smartrecorderapp.realtime_database.RealtimeDBState
import com.example.smartrecorderapp.viewmodels.FirebaseDBViewModel
import com.example.smartrecorderapp.viewmodels.FirebaseStorageViewModel
import com.example.smartrecorderapp.viewmodels.ViewModelLDB
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.vertexai.type.Content
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    navController: NavHostController,
    lessonLDB: ViewModelLDB,
    innerPadding: PaddingValues,
    context: Context = LocalContext.current,
    firebaseDb: FirebaseDBViewModel = hiltViewModel(),
    firebaseStorage: FirebaseStorageViewModel = hiltViewModel()
) {
    //
    val recorder by lazy { AndroidAudioRecorder(context) }
    val player by lazy { AndroidAudioPlayer(context) }

    DisposableEffect(Unit) {
        onDispose {
            Log.e("DEBUGGE", "Left lesson screen")
            recorder.stop()
            player.stop()
        }
    }
    //
    val debugData = lessonLDB.getRememberedData()
    ////


    val audioFile = File(context.cacheDir, "audio_${debugData[3]}_${debugData[1]}.mp3")

    val dbState by firebaseDb.dbState.collectAsState()

    val recordAvaliable = ( dbState == RealtimeDBState.Error("Запись не обнаружена")
            || dbState == RealtimeDBState.Error( "Запись удалена") )
    val playAvaliable = dbState is RealtimeDBState.Idle

    ///
    var isRecordAllowed by remember {
        mutableStateOf(checkPermissionFor(context, Manifest.permission.RECORD_AUDIO) )
    }
    val getAudioPermission = rememberLauncherForActivityResult( contract = ActivityResultContracts.RequestPermission() ) {
        isRecordAllowed = it
    }
    //

    if ( !audioFile.exists() ) {
        firebaseStorage.getLesson(
            debugData[3],
            debugData[1].toLong(),
            audioFile
        )
    }
    else Log.i("DEBUGGE", "File's found in cache")


    Column(
        Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Row (
            Modifier
                .padding(0.dp, 10.dp, 0.dp, 0.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IconButton(
                enabled = recordAvaliable,
                onClick = {
                    if (!isRecordAllowed) {
                        getAudioPermission.launch(Manifest.permission.RECORD_AUDIO)
                    } else {
                        recorder.start(audioFile)

                    }
                },
            ) {
                Icon(
                    painterResource(R.drawable.mic_icon), "Кнопка записи",
                    modifier = Modifier.requiredSize(30.dp),
                    tint = if (recordAvaliable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }

            IconButton(
                enabled = recordAvaliable,
                onClick = {
                    recorder.stop()
                    firebaseDb.addLesson(
                        debugData[3],
                        debugData[1].toLong(),
                        audioFile
                    )

                    firebaseStorage.addLesson(
                        debugData[3],
                        debugData[1].toLong(),
                        audioFile
                    )
                },
            ) {
                Icon(
                    Icons.Filled.Close,
                    "Кнопка остановки записи",
                    tint = if (recordAvaliable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
            IconButton(
                enabled = playAvaliable,
                onClick = {
                    if (audioFile.exists()) {
                        player.playFile(audioFile)
                    } else Log.e("DEBUGGE", "File does not exist")
                },
            ) {
                Icon(
                    Icons.Filled.PlayArrow,
                    "Кнопка воспроизведения записи",
                    tint = if (playAvaliable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
            IconButton(
                enabled = playAvaliable,
                onClick = {
                    audioFile.delete()
                    firebaseDb.removeLesson(
                        debugData[3],
                        debugData[1].toLong()
                    )
                    firebaseStorage.removeLesson(
                        debugData[3],
                        debugData[1].toLong()
                    )
                },
            ) {
                Icon(
                    Icons.Filled.Delete,
                    "Кнопка удаления записи",
                    tint = if (playAvaliable) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
        ///
        Surface ( color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .padding(15.dp )
                .fillMaxWidth().
                wrapContentHeight(),
            shape = MaterialTheme.shapes.medium
        ) {
            when ( dbState ) {
                is RealtimeDBState.Error -> {
                    Log.i("DEBUGGE", "Error")
                    Toast.makeText(context, "${(dbState as RealtimeDBState.Error).errorMessage}",
                        Toast.LENGTH_SHORT).show()
//                    recordAvaliable = true
//                    playAvaliable = false
                }
                is RealtimeDBState.Idle -> {
                    Log.i("DEBUGGE", "Loaded")
                    Text((dbState as RealtimeDBState.Idle).transcription ?: "", Modifier.padding(10.dp, 6.dp))
//                    recordAvaliable = false
//                    playAvaliable = true
                }

                RealtimeDBState.InProgress -> {
                    Log.i("DEBUGGE", "InProgress...")
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(15.dp)) {
                        CircularProgressIndicator( modifier = Modifier.size(50.dp))
                    }
                }
                RealtimeDBState.None -> {
                    firebaseDb.getLesson(debugData[3], debugData[1].toLong())
                    Log.i("DEBUGGE", "None")
                }

            }
        }


        ///
    }

}

fun checkPermissionFor(activity: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
}
