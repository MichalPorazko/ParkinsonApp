package com.example.ParkinsonApp.DataTypes

sealed class UserType {
    abstract val email: String

    data class PatientUser(
        override val email: String,
        val patientData: PatientData = PatientData()
    ) : UserType()

    data class DoctorUser(
        override val email: String,
        val doctorData: DoctorData = DoctorData()
    ) : UserType()
}

data class PatientData(
    val firstName: String = "",
    val lastName: String = "",
    var medications: MedicationSchedule = MedicationSchedule(),
    var waterIntake: Water = Water(),
    var emotions: List<Emotion> = emptyList()
){
}

data class DoctorData(
    val firstName: String = "",
    val lastName: String = "",
    val pwzNumber: Int = 0,
    val patients: List<String> = emptyList()  // List of patient IDs
){
}

data class MedicationSchedule(
    var medications: List<Medication> = emptyList()
){
}

data class Medication(
    val name: String,
    val dose: String,
    val times: List<Hour> = emptyList()
){
}

data class Hour(
    val hour: Int,
    val minute: Int,
    var taken: Boolean = false
){

}

data class Water(
    var amount: Int = 0
){

}

data class Emotion(
    val name: String,
    val state: EmotionState
){

}

enum class EmotionState {
    VERY_SAD, SAD, NEUTRAL, GOOD, VERY_GOOD
}

data class PatientDataWithId(
    val id: String = "",
    val data: PatientData = PatientData()
)

data class DoctorDataWithId(
    val id: String = "",
    val data: DoctorData = DoctorData()
)

data class User(
    val email: String = "",
    val userType: String = "",
    val patientData: PatientData? = null,
    val doctorData: DoctorData? = null
)