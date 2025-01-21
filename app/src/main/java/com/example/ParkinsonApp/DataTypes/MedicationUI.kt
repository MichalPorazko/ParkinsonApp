package com.example.ParkinsonApp.DataTypes

data class MedicationUI(
    val name: String,
    val dosage: String,
    val time: String
)

data class ScheduleEntry(
    val time: String,
    val medications: List<Medication>
)