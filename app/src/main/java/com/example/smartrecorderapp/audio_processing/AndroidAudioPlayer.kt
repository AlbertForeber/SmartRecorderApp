package com.example.smartrecorderapp.audio_processing

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File

class AndroidAudioPlayer(
    private val context: Context
): AudioPlayer {
    private var player: MediaPlayer? = null
    override fun playFile(file: File) {
        MediaPlayer.create(context, file.toUri()).apply {
            player = this
            player?.setVolume(5f, 5f)
            start()
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}