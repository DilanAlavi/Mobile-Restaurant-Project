package com.ucb.ucbtest.di

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AndroidApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        //Inicializae Firebase
        FirebaseApp.initializeApp(this)
    }
}