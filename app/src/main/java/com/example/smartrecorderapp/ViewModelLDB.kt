package com.example.smartrecorderapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ViewModelLDB(application: Application): AndroidViewModel(application) {
    private val daysDaoModel = LessonsDB.getLessonDB(application)?.daysDao()

    fun insertDay(id: Int, lessonId: Int, lessonString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            daysDaoModel?.insertDay(Day(id = id, lessonId = lessonId, lessons = lessonString))
        }
    }

    fun updateDay(id: Int, lessonId: Int, lessonString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            daysDaoModel?.updateDay(Day(id = id, lessonId = lessonId, lessons = lessonString))
        }
    }

    fun deleteLessonById(id: Int, lessonId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            daysDaoModel?.deleteLessonByID(id, lessonId)
            Log.e("RemoveDB", "Received!")
        }
    }
    fun getDay(id: Int, lessons: (lessons: List<Day>) -> Unit) {
        var day: List<Day>?
        viewModelScope.launch(Dispatchers.IO) {
            day = daysDaoModel?.getDay(id)
            day?.let { lessons(it) }
        }
    }
}