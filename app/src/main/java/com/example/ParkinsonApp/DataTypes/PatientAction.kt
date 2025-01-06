package com.example.ParkinsonApp.DataTypes

data class PatientAction(
    val patientFirstName: String,
    val patientLastName: String,
    val patientImageRes: Int, // Resource ID for the patient's photo
    val actionDescription: String,
    val actionIconRes: Int // Resource ID for the action icon
)
