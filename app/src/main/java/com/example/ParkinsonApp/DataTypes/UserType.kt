package com.example.ParkinsonApp.DataTypes

sealed class UserType {
    data class PatientUser(
        val email: String,
        val password: String,
        val patientData: PatientData

    ) : UserType()

    data class MedicationSchedule(
        val medication: List<Medication>
    )

    data class Medication(
        val time: List<Hour>,
        val dose: String,
        val name: String
    )

    data class Hour(
        val hour: Int,
        val minute: Int,
        val taken: Boolean
    )

    data class Water(
        val amount: Int
    )

    data class Emotion(
        val name: String,
        val state: EmotionState
    )

    enum class EmotionState {
        VERY_SAD, SAD, NETURAL, GOOD, VERY_GOOD
    }

    data class PatientData(
        val firstName: String,
        val lastName: String,
        val schedule: MedicationSchedule?,
        val water: Water?,
        val emotions: List<Emotion>
    )

    data class DoctorUser(
        val firstName: String,
        val lastName: String,
        val email: String,
        val password: String,
        val PWZNumber: Int,
        val patients: List<PatientData>
    ) : UserType()
}
