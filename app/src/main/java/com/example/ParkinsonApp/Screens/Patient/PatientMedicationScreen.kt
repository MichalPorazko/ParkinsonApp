package com.example.ParkinsonApp.Screens.Patient

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.ParkinsonApp.DataTypes.Medication
import com.example.ParkinsonApp.DataTypes.MedicationCard
import com.example.ParkinsonApp.DataTypes.ScheduleEntry
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import com.example.ParkinsonApp.Navigation.NavRoute
import com.example.ParkinsonApp.Navigation.SharedViewModel
import com.example.ParkinsonApp.R

@Composable
fun PatientMedicationScreen(patientName: String,
                            yearBorn: Int,
                            yearDiagnosed: Int,
                            medications: List<Medication>,
                            schedule: List<ScheduleEntry>,
                            onShareClick: () -> Unit) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            // Header Section
            Text(
                text = "Medication Card",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = patientName,
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Red),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Born: $yearBorn",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Diagnosed: $yearDiagnosed",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Share Button
            Button(
                onClick = onShareClick,
                colors = ButtonDefaults.buttonColors(Color(0xFFEE6A5F)),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Share")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Medication List
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(medications) { medication ->
                    MedicationItem(medication)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Schedule Section
            Column(modifier = Modifier.fillMaxWidth()) {
                schedule.forEach { entry ->
                    ScheduleRow(entry)
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer Section
            Button(
                onClick = { /* TODO: Add functionality */ },
                colors = ButtonDefaults.buttonColors(Color(0xFFFF9900)),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Parkinson's ON")
            }
        }
    }

    @Composable
    fun MedicationItem(medication: Medication) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(80.dp)
                .padding(8.dp)
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Icon(
                painter = painterResource(id = medication.icon),
                contentDescription = medication.name,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = medication.name,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = medication.dosage,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    fun ScheduleRow(entry: ScheduleEntry) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF9F9F9), shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = entry.time,
                style = MaterialTheme.typography.titleLarge
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(entry.medications) { medication ->
                    Icon(
                        painter = painterResource(id = medication.icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }

    // Sample Usage
    @Composable
    fun MedicationCardPreview() {
        val medications = listOf(
            Medication("Madopar", "50mg/12.5mg", R.drawable.medication_24px),
            Medication("Stalevo", "75mg/200mg", R.drawable.medication_24px),
            Medication("Ibuprofen", "50mg", R.drawable.medication_24px),
            Medication("Custom10", "10mg", R.drawable.medication_24px)
        )
        val schedule = listOf(
            ScheduleEntry("07:00", listOf(medications[0], medications[1])),
            ScheduleEntry("10:00", listOf(medications[2])),
            ScheduleEntry("16:00", listOf(medications[3]))
        )

        MedicationCard(
            patientName = "Kyle",
            yearBorn = 1983,
            yearDiagnosed = 2005,
            medications = medications,
            schedule = schedule,
            onShareClick = { /* Handle share action */ }
        )

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPatientMedicationScreen() {
    val sharedViewModel = SharedViewModel(firebaseRepository = FirebaseRepository())
    PatientMedicationScreen(
        patientName = "Kyle",
        yearBorn = 1983,
        yearDiagnosed = 2005,
        medications = listOf(
            Medication("Madopar", "50mg/12.5mg", R.drawable.medication_24px),
            Medication("Stalevo", "75mg/200mg", R.drawable.medication_24px),
            Medication("Ibuprofen", "50mg", R.drawable.medication_24px),
            Medication("Custom10", "10mg", R.drawable.medication_24px)
        ),
        schedule = listOf(
            ScheduleEntry("07:00", listOf(Medication("Madopar", "50mg/12.5mg", R.drawable.medication_24px))),
            ScheduleEntry("10:00", listOf(Medication("Stalevo", "75mg/200mg", R.drawable.medication_24px))),
            ScheduleEntry("16:00", listOf(Medication("Ibuprofen", "50mg", R.drawable.medication_24px)))
        )
    ) { }
}