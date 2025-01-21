package com.example.ParkinsonApp.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ParkinsonApp.DataTypes.ActionType
import com.example.ParkinsonApp.DataTypes.EmotionState
import com.example.ParkinsonApp.DataTypes.MedicationSchedule
import com.example.ParkinsonApp.DataTypes.MedicationUI
import com.example.ParkinsonApp.DataTypes.PatientAction
import com.example.ParkinsonApp.DataTypes.PatientData
import com.example.ParkinsonApp.DataTypes.PatientDataWithId
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PatientViewModel(
    private val firebaseRepository: FirebaseRepository,
) : ViewModel() {

    private val _waterIntake = MutableLiveData<Int>(0)
    val waterIntake: LiveData<Int> get() = _waterIntake
    val patientId = firebaseRepository.getCurrentUserId()!!
    private val _patientData = MutableStateFlow<PatientDataWithId?>(null)
    val patientData: StateFlow<PatientData?> get() = _patientData.map { it?.data }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val nextMedicationDetail: StateFlow<MedicationUI?> = _patientData.map { data ->
        calculateNextMedication(data?.data?.medications)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)


    init {
        loadPatientData()
    }

    private fun loadPatientData() {
        viewModelScope.launch {
            firebaseRepository.getPatientData(patientId) { data ->
                data?.let {
                    _patientData.value = it
                }
            }
        }
    }

    fun updateWaterIntake() {
        firebaseRepository.updateWaterIntake(patientId, _waterIntake.value ?: 0) { success ->
            if (success) {
                // Log the action
                val patientData = patientData.value // Assuming you have access to patient's first and last name
                val actionDescription = "Updated water intake to $_waterIntake glasses."
                val patientAction = PatientAction(
                    patientId = patientId,
                    patientFirstName = patientData?.firstName ?: "",
                    patientLastName = patientData?.lastName ?: "",
                    actionDescription = actionDescription,
                    actionType = ActionType.WATER_INTAKE_UPDATED
                )
                firebaseRepository.logPatientAction(patientAction) { /* Handle result if needed */ }
            }
        }
    }

    fun incrementWaterIntake() {
        _waterIntake.value = (_waterIntake.value ?: 0) + 1
        updateWaterIntake()
    }

    fun decrementWaterIntake() {
        _waterIntake.value = (_waterIntake.value ?: 0).coerceAtLeast(1) - 1
        updateWaterIntake()
    }

    // Handle state face click
    fun onStateFaceClicked(emotionName: String, emotionState: EmotionState) {
        val patientId = firebaseRepository.getCurrentUserId() ?: return
        val updatedEmotions = _patientData.value?.data?.emotions?.map {
            if (it.name == emotionName) {
                it.copy(state = emotionState)
            } else {
                it
            }
        } ?: listOf()

        // Update local data
        _patientData.value = _patientData.value?.apply {
            data.emotions = updatedEmotions
        }

        // Update in Firestore
        firebaseRepository.updateEmotions(patientId, updatedEmotions) { success ->
            if (!success) {
                // Handle error
            }
        }
    }

    private fun calculateNextMedication(medications: MedicationSchedule?): MedicationUI? {
        medications?.medications?.flatMap { medication ->
            medication.times.filterNot { it.taken }.map { time ->
                MedicationUI(
                    name = medication.name,
                    dosage = medication.dose ,
                    time = "${time.hour}:${time.minute}"
                )
            }
        }?.minByOrNull { detail ->
            // Compare times to find the earliest
            val (hour, minute) = detail.time.split(":").map { it.toInt() }
            hour * 60 + minute
        }?.let { nextMedication ->
            return nextMedication
        }
        return null
    }
}