package com.example.smartrecorderapp.audio_processing

import android.media.AudioProfile
import android.media.EncoderProfiles
import android.media.MediaRecorder
import android.os.Build
import com.google.firebase.database.core.Context
import java.io.File
import java.io.FileOutputStream

class AndroidAudioRecorder(private val context: android.content.Context) : AudioRecorder {
    private var recorder: MediaRecorder? = null

    private fun initializeRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else MediaRecorder()
    }

    override fun start(outputFile: File) {
        initializeRecorder().apply {
            setOutputFile(FileOutputStream(outputFile).fd)
            setAudioSource(MediaRecorder.AudioSource.UNPROCESSED)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(256 * 1024)
            setAudioSamplingRate(44100)
            prepare()
            start()
            recorder = this
        }
    }

    override fun stop() {
        recorder?.stop()
        recorder?.reset()
        recorder = null
    }
}