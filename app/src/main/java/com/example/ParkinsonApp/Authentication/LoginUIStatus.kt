package com.example.ParkinsonApp.Authentication

import android.util.Log

data class LoginUIStatus(

    var email  :String = "",
    var password  :String = "",

    var emailError :Boolean = false,
    var passwordError : Boolean = false
){

    init {
        Log.d("LoginUIStatus", "Initialized with email: $email, password: $password, emailError: $emailError, passwordError: $passwordError")
    }
}
