package com.example.ParkinsonApp.DataTypes

sealed class UserType {
    abstract val email: String
    abstract val password: String
    abstract val firstName: String
    abstract val lastName: String

    data class PatientUser(
        override val email: String,
        override val password: String,
        override val firstName: String,
        override val lastName: String,
        val patientData: PatientData = PatientData()
    ) : UserType()

    data class DoctorUser(
        override val email: String,
        override val password: String,
        override val firstName: String,
        override val lastName: String,
        val doctorData: DoctorData = DoctorData()
    ) : UserType()
}

data class PatientData(
    var medications: MedicationSchedule = MedicationSchedule(),
    var waterIntake: Water = Water(),
    var emotions: List<Emotion> = emptyList()
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "medications" to medications.toMap(),
            "waterIntake" to waterIntake.toMap(),
            "emotions" to emotions.map { it.toMap() }
        )
    }
}

data class DoctorData(
    val pwzNumber: Int = 0,
    val patients: List<PatientData> = emptyList()  // List of patient IDs
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "pwzNumber" to pwzNumber,
            "patients" to patients.map { it.toMap() }
        )
    }
}

data class MedicationSchedule(
    var medications: List<Medication> = emptyList()
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "medications" to medications.map { it.toMap() }
        )
    }
}

data class Medication(
    val name: String,
    val dose: String,
    val times: List<Hour> = emptyList()
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "dose" to dose,
            "times" to times.map { it.toMap() }
        )
    }
}

data class Hour(
    val hour: Int,
    val minute: Int,
    var taken: Boolean = false
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "hour" to hour,
            "minute" to minute,
            "taken" to taken
        )
    }
}

data class Water(
    var amount: Int = 0
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "amount" to amount
        )
    }
}

data class Emotion(
    val name: String,
    val state: EmotionState
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "name" to name,
            "state" to state.name
        )
    }
}

enum class EmotionState {
    VERY_SAD, SAD, NEUTRAL, GOOD, VERY_GOOD
}