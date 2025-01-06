package com.example.ParkinsonApp.DataTypes


class MedicationCard(
    patientName: String,
    yearBorn: Int,
    yearDiagnosed: Int,
    medications: List<Medication>,
    schedule: List<ScheduleEntry>,
    onShareClick: () -> Unit
)

data class Medication(
    val name: String,
    val dosage: String,
    val icon: Int
)

data class ScheduleEntry(
    val time: String,
    val medications: List<Medication>
)