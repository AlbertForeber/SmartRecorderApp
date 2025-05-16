package com.example.smartrecorderapp

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseManager {
    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuth = Firebase.auth

    @Singleton
    @Provides
    fun provideFireBaseDB(): FirebaseDatabase = Firebase.database

    @Singleton
    @Provides
    fun provideFireBaseStorage(): FirebaseStorage = Firebase.storage

}