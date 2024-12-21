package com.example.sensors.Navigation

// SharedViewModel.kt
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.sensors.Firebase.FirebaseRepository


class SharedViewModel(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {


    fun signUpUser(
        email: String,
        password: String,
        userType: String,
        additionalInfo: Map<String, String>
    ) {
        firebaseRepository.signup(email, password, userType, additionalInfo)
    }

    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        firebaseRepository.login(email, password)
        firebaseRepository.authState.observeForever { authState ->
            when (authState) {
                is FirebaseRepository.AuthState.Authenticated -> onSuccess()
                is FirebaseRepository.AuthState.Error -> onError(authState.message)
                else -> {}
            }
        }
    }


    fun signOut() {
        firebaseRepository.signOut()
    }

    fun refreshData() {
        firebaseRepository.refreshData()
    }


}

