package com.example.sensors.Authentication

data class LoginUIStatus(

    var email  :String = "",
    var password  :String = "",

    var emailError :Boolean = false,
    var passwordError : Boolean = false
)
