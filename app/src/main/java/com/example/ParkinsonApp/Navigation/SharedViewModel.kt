package com.example.ParkinsonApp.Navigation

// SharedViewModel.kt
import androidx.lifecycle.ViewModel
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ParkinsonApp.DataTypes.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SharedViewModel(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients: StateFlow<List<Patient>> get() = _patients

    init {
        loadPatients()
    }

    private fun loadPatients() {
        viewModelScope.launch {
            val patientList = firebaseRepository.getPatients()
            _patients.value = patientList
        }
    }

    fun refreshData() {
        firebaseRepository.refreshData()
    }

    // Handle state face click
    fun onStateFaceClicked(state: String, faceIndex: Int) {
        // Implement logic to handle the selected face for the state
        // For example, save the data to the repository or update LiveData
    }

    // Increment water intake
    private val _waterIntake = MutableLiveData<Int>(0)
    val waterIntake: LiveData<Int> get() = _waterIntake

    fun incrementWaterIntake() {
        _waterIntake.value = (_waterIntake.value ?: 0) + 1
    }

    fun decrementWaterIntake() {
        _waterIntake.value = (_waterIntake.value ?: 0).coerceAtLeast(1) - 1
    }

    fun getPatientById(patientId: String): StateFlow<Patient?> {
        val patientFlow = MutableStateFlow<Patient?>(null)
        viewModelScope.launch {
            val patient = firebaseRepository.getPatientById(patientId)
            patientFlow.value = patient
        }
        return patientFlow
    }

}



