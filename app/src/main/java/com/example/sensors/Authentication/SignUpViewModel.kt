package com.example.sensors.Authentication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.sensors.Firebase.FirebaseRepository
import android.util.Log

class SignupViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    private val TAG = SignupViewModel::class.simpleName


    var registrationUIState = mutableStateOf(SignUpUIStatus())


    var allValidationsPassed = mutableStateOf(false)
    init {
            Log.d(TAG, "the value is ${allValidationsPassed.value}")

    }

    var signUpInProgress = mutableStateOf(false)

    fun onEvent(event: SignUpUIEvent) {
        Log.d(TAG, "onEvent called with: $event")
        when (event) {
            is SignUpUIEvent.FirstNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    firstName = event.firstName
                )
            }

            is SignUpUIEvent.LastNameChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    lastName = event.lastName
                )
            }

            is SignUpUIEvent.EmailChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    email = event.email
                )

            }


            is SignUpUIEvent.PasswordChanged -> {
                registrationUIState.value = registrationUIState.value.copy(
                    password = event.password
                )

            }

            is SignUpUIEvent.SignUpButtonClicked -> {
                Log.d(TAG, "button register clicked")
                signUp()
            }

        }
        validateDataWithRules()
    }

    private fun validateDataWithRules() {
        val fNameResult = ValidationRules.validateFirstName(
            fName = registrationUIState.value.firstName
        )

        val lNameResult = ValidationRules.validateLastName(
            lName = registrationUIState.value.lastName
        )

        val emailResult = ValidationRules.validateEmail(
            email = registrationUIState.value.email
        )

        val passwordResult = ValidationRules.validatePassword(
            password = registrationUIState.value.password
        )


        // Log validation results
        Log.d(TAG, "Validation Results: fName=${fNameResult.status}, lName=${lNameResult.status}, email=${emailResult.status}, password=${passwordResult.status}")

        registrationUIState.value = registrationUIState.value.copy(
            firstNameError = fNameResult.status,
            lastNameError = lNameResult.status,
            emailError = emailResult.status,
            passwordError = passwordResult.status
        )

        allValidationsPassed.value = fNameResult.status && lNameResult.status &&
                emailResult.status && passwordResult.status
        // Log the final value of allValidationsPassed
        Log.d(TAG, "allValidationsPassed updated to ${allValidationsPassed.value}")
    }


    private fun signUp() {
        firebaseRepository.signup(
            email = registrationUIState.value.email,
            password = registrationUIState.value.password,
            userType = "user",
            additionalInfo = mapOf(
                "firstName" to registrationUIState.value.firstName,
                "lastName" to registrationUIState.value.lastName
            )
        )
    }




}