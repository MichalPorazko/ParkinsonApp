package com.example.ParkinsonApp.Authentication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ParkinsonApp.Firebase.FirebaseRepository

class LoginViewModel(private val firebaseRepository: FirebaseRepository): ViewModel() {

    val loginUIState = mutableStateOf(LoginUIStatus())

    val validationRules = mutableStateOf(false)

    val loginInProgress = mutableStateOf(false)

    fun onEvent(event: LoginUIEvent) {
        validateLoginUIDataWithRules()
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
            }

            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
            }

            is LoginUIEvent.LoginButtonClicked -> {
                firebaseRepository.login(loginUIState.value.email, loginUIState.value.password)
            }
        }

    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = ValidationRules.validateEmail(
            email = loginUIState.value.email
        )

        val passwordResult = ValidationRules.validatePassword(
            password = loginUIState.value.password
        )

        loginUIState.value = loginUIState.value.copy(
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        validationRules.value = emailResult.status && passwordResult.status


    }


}