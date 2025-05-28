package com.example.smartrecorderapp.audio_processing

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import java.io.File

class AndroidAudioPlayer(
    private val context: Context
): AudioPlayer {
    private var player: MediaPlayer? = null
    override fun playFile(file: File) {
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes
                    .Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
            )
            setVolume(1f, 1f)
            setDataSource(context, file.toUri())
            prepare()
            player = this
            Log.i("DEBUGGE", "Started player")
            start()
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}