package com.example.smartrecorderapp

import com.example.smartrecorderapp.authentication.AuthRepository
import com.example.smartrecorderapp.authentication.AuthRepositoryImpl
import com.example.smartrecorderapp.realtime_database.RealtimeDBRepository
import com.example.smartrecorderapp.realtime_database.RealtimeDBRepositoryImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
/*
Используем abstract для использования @Binds - более лаконичный способ связать реализацию класса
с интерфейсом (если где-то требуется AuthRepository, предоставляем AuthRepositoryImpl и т.д
 */
abstract class FirebaseManager {
    // Нужно использовать companion object, так как функции @Provides не могут быть абстрактными
    companion object {
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

    // Ставить Singleton над @Binds не имеет смысла (игнорируется), так как метод не является
    // методом создания экземпляра в отличие от методов @Provides. Ставится в самой реализации
    @Binds
    abstract fun bindAuthRepository( impl: AuthRepositoryImpl ): AuthRepository

    @Binds
    abstract fun bindRealtimeDBRepository( impl: RealtimeDBRepositoryImpl ): RealtimeDBRepository

}