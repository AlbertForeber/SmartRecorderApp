package com.example.smartrecorderapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewModelLDB(application: Application): AndroidViewModel(application) {
    private val daysDaoModel = LessonsDB.getLessonDB(application)?.daysDao()
    private val allDays = daysDaoModel?.getDays()

    fun insertDay(id: Int, lessonString: String) {
        viewModelScope.launch(Dispatchers.IO) {
            daysDaoModel?.insertDay(Day(id = id, lessons = lessonString))
        }
    }

    fun deleteDayById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            daysDaoModel?.deleteDayByID(id)
        }
    }

    fun getDays(): LiveData<List<Day>>? {
        return allDays
    }
}