package com.example.smartrecorderapp.database

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartrecorderapp.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class ViewModelLDB @Inject constructor(
    @ApplicationContext private val application: Context
): ViewModel() {
    private val daysDaoModel = LessonsDB.getLessonDB(application)?.daysDao()
    private var rememberData = mutableListOf("", "", "")
    var isSelectingDate = mutableStateOf(false)
    var isDialog = mutableStateOf(false)
    var selectedData = mutableLongStateOf(System.currentTimeMillis())

    fun setRememberedData(dataMillis: Long, lessonID: Int, lessonName: String) {
        rememberData[0] = dataMillis.toString()
        rememberData[1] = lessonID.toString()
        rememberData[2] = lessonName
    }
    fun getRememberedData(): MutableList<String> {
        return rememberData
    }

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