package com.example.ParkinsonApp.DataTypes


class MedicationCard(
    patientName: String,
    yearBorn: Int,
    yearDiagnosed: Int,
    medications: List<MedicationTest>,
    schedule: List<ScheduleEntry>,
    onShareClick: () -> Unit
)

data class MedicationTest(
    val name: String,
    val dosage: String,
    val icon: Int
)

data class ScheduleEntry(
    val time: String,
    val medications: List<MedicationTest>
)