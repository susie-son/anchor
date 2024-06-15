package com.susieson.anchor.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {
    @Provides
    fun provideFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }
}
