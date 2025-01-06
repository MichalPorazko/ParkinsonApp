package com.example.ParkinsonApp.Authentication

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ParkinsonApp.Firebase.FirebaseRepository

class LoginViewModel(private val firebaseRepository: FirebaseRepository): ViewModel() {

    val loginUIState = mutableStateOf(LoginUIStatus())

    val validationRules = mutableStateOf(false)

    val loginInProgress = mutableStateOf(false)

    init {
        Log.d(TAG, "Initial loginUIState: ${loginUIState.value}")
        Log.d(TAG, "Initial validationRules: ${validationRules.value}")
        Log.d(TAG, "Initial loginInProgress: ${loginInProgress.value}")
    }

    fun onEvent(event: LoginUIEvent) {
        Log.d(TAG, "onEvent called with: $event")
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    email = event.email
                )
                Log.d(TAG, "Updated email: ${loginUIState.value.email}")
            }

            is LoginUIEvent.PasswordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
                Log.d(TAG, "Updated password: ${loginUIState.value.password}")
            }

            is LoginUIEvent.LoginButtonClicked -> {
                Log.d(TAG, "Login button clicked")
                firebaseRepository.login(loginUIState.value.email, loginUIState.value.password)
            }
        }
        validateLoginUIDataWithRules()

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
        Log.d(TAG, "Validation Results: email=${emailResult.status}, password=${passwordResult.status}")

        validationRules.value = emailResult.status && passwordResult.status
        Log.d(TAG, "validationRules updated to ${validationRules.value}")


    }


}