package com.example.smartrecorderapp.audio_processing

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}