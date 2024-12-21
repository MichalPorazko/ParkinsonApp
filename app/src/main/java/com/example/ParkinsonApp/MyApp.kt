package com.example.ParkinsonApp

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        Firebase.firestore
    }
}
