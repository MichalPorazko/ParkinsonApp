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

    init {
        loadDoctorData()
    }


    private fun loadDoctorData() {
        val doctorId = firebaseRepository.getCurrentUserId()
        if (doctorId != null) {
            firebaseRepository.getDoctorData(doctorId) { doctorDataWithId ->
                if (doctorDataWithId != null) {
                    _doctorData.value = doctorDataWithId
                    // After loading doctor data, load patients data
                    loadPatientsData(doctorDataWithId.data.patients)
                }
            }
        }
    }

    private fun loadPatientsData(patientIds: List<String>) {
        if (patientIds.isNotEmpty()) {
            firebaseRepository.getPatientsData(patientIds) { patients ->
                _patients.value = patients
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