package com.example.smartrecorderapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.smartrecorderapp.database.LessonFDB
import com.example.smartrecorderapp.database.LessonsDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import jakarta.inject.Inject

class FirebaseDBViewModel @Inject constructor(
    @Inject private val firebaseDatabase: FirebaseDatabase,
    @Inject private val auth: FirebaseAuth
) : ViewModel( ) {
    val reference = firebaseDatabase.reference
    fun addLesson( dateInMillis: Long, lessonId: Int, pathInStorage: String, transcription: String ) {
        val lessonFDB = LessonFDB( lessonId, pathInStorage, transcription )
        reference.child("users").child("${auth.currentUser?.uid}").child("$dateInMillis").setValue( lessonFDB )
    }

    fun getLesson( dateInMillis: Long, lessonId: Int ): LessonFDB {
        val reference = reference
            .child("users")
            .child("${auth.currentUser?.uid}")
            .child("$dateInMillis")
            .get()
        reference.result.children


    }


    fun removeLesson() {

    }
}