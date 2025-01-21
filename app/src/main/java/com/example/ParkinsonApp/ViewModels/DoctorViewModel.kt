package com.example.ParkinsonApp.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ParkinsonApp.DataTypes.DoctorDataWithId
import com.example.ParkinsonApp.DataTypes.PatientDataWithId
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DoctorViewModel(
    private val firebaseRepository: FirebaseRepository,
) : ViewModel() {

    private val _patients = MutableStateFlow<List<PatientDataWithId>>(emptyList())
    val patients: StateFlow<List<PatientDataWithId>> get() = _patients
    private val _doctorData = MutableStateFlow<DoctorDataWithId?>(null)
    val doctorData: StateFlow<DoctorDataWithId?> get() = _doctorData
    val doctorId = firebaseRepository.getCurrentUserId()

    init {
        loadPatients()
        loadDoctorData()
    }

    private fun loadPatients() {

        if (doctorId != null) {
            viewModelScope.launch {
                firebaseRepository.getPatientsData(doctorId) { patients ->
                    _patients.value = patients
                }
            }
        } else {
            // Handle the case where the user is not authenticated
            _patients.value = emptyList()
        }
    }

    private fun loadDoctorData() {
        if (doctorId != null) {
            viewModelScope.launch {
                firebaseRepository.getDoctorData(doctorId) { data ->
                    data?.let {
                        _doctorData.value = it
                    }
                }
            }
        }
    }


    fun getPatientById(patientId: String): StateFlow<PatientDataWithId?> {
        val patientFlow = MutableStateFlow<PatientDataWithId?>(null)
        viewModelScope.launch {
            firebaseRepository.getPatientData(patientId) { data ->
                data?.let {
                    patientFlow.value = it
                }
            }
        }
        return patientFlow
    }
}