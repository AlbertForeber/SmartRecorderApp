package com.example.smartrecorderapp.audio_processing

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}