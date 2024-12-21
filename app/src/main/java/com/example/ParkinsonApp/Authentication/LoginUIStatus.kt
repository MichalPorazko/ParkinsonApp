package com.example.ParkinsonApp.Authentication

data class LoginUIStatus(

    var email  :String = "",
    var password  :String = "",

    var emailError :Boolean = false,
    var passwordError : Boolean = false
)
