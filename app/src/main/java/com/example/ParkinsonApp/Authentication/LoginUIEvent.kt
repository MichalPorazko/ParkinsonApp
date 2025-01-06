package com.example.ParkinsonApp.Authentication

import android.util.Log

sealed class LoginUIEvent {
    data class EmailChanged(val email: String) : LoginUIEvent() {
        init {
            Log.d("LoginUIEvent", "EmailChanged event with email: $email")
        }
    }
    data class PasswordChanged(val password: String) : LoginUIEvent() {
        init {
            Log.d("LoginUIEvent", "PasswordChanged event with password: $password")
        }
    }
    object LoginButtonClicked : LoginUIEvent() {
        init {
            Log.d("LoginUIEvent", "LoginButtonClicked event")
        }
    }
}