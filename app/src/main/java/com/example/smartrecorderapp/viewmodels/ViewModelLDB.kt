package com.example.smartrecorderapp.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartrecorderapp.database.Day
import com.example.smartrecorderapp.database.LessonsDB
import com.example.smartrecorderapp.date_functional.StartDateReference
import com.example.smartrecorderapp.date_functional.formatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class ViewModelLDB @Inject constructor(
    @ApplicationContext private val application: Context,
    val sharedPrefs: StartDateReference
): ViewModel() {
    private val daysDaoModel = LessonsDB.Companion.getLessonDB(application)?.daysDao()
    private var rememberData = mutableListOf("", "", "", "")
    var isSelectingDate = mutableStateOf(false)
    var isDialog = mutableStateOf(false)
    var startDate = mutableStateOf(sharedPrefs.startDate)
    var selectedData = mutableLongStateOf(System.currentTimeMillis())
    var actualWeek = mutableStateOf(0)

    fun setWeek() {
        if (startDate.value != null) {
            actualWeek.value = ((selectedData.longValue - startDate.value!!) / 604800000).toInt() + 1
        }
    }

    fun setRememberedData(dataMillis: Long, lessonID: Int, lessonName: String) {
        rememberData[0] = dataMillis.toString()
        rememberData[1] = lessonID.toString()
        rememberData[2] = lessonName
        rememberData[3] = formatDate(dataMillis)[2].replace('.', '-')
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
            Log.i("DEBUGGE", "${id + 7 * ( (actualWeek.value - 1) % 2 )}")
            day = daysDaoModel?.getDay(id + 7 * ( (actualWeek.value - 1) % 2 ))
            day?.let { lessons(it) }
        }
    }
}