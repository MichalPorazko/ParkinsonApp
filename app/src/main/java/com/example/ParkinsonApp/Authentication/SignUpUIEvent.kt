package com.example.ParkinsonApp.Authentication

sealed class SignUpUIEvent {

    data class FirstNameChanged(val firstName:String) : SignUpUIEvent()
    data class LastNameChanged(val lastName:String) : SignUpUIEvent()
    data class EmailChanged(val email:String): SignUpUIEvent()
    data class PasswordChanged(val password: String) : SignUpUIEvent()

    object SignUpButtonClicked :SignUpUIEvent()
}