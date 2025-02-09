package com.example.ParkinsonApp.Screens.Patient

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.ParkinsonApp.DataTypes.Medication
import com.example.ParkinsonApp.DataTypes.ScheduleEntry
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import com.example.ParkinsonApp.R
import com.example.ParkinsonApp.ViewModels.PatientViewModel


@Composable
fun PatientMedicationScreen(
    sharedViewModel: PatientViewModel,
    paddingValues: PaddingValues,
    onShareClick: () -> Unit
) {
    val patientData by sharedViewModel.patientData.collectAsState()
    val patientName = "${patientData?.firstName ?: ""} ${patientData?.lastName ?: ""}"
    val medications = patientData?.medications?.medications ?: listOf()

    // Generate schedule entries
    val scheduleEntries = medications.flatMap { medication ->
        medication.times.map { time ->
            ScheduleEntry(
                time = "${time.hour}:${time.minute.toString().padStart(2, '0')}",
                medications = listOf(medication)
            )
        }
    }.sortedBy { entry ->
        val (hour, minute) = entry.time.split(":").map { it.toInt() }
        hour * 60 + minute
    }

    MedicationCard(
        patientName = patientName,
        medications = medications,
        schedule = scheduleEntries,
        onShareClick = onShareClick,
        paddingValues = paddingValues
    )
}

@Composable
fun MedicationCard(
    patientName: String,
    medications: List<Medication>,
    schedule: List<ScheduleEntry>,
    onShareClick: () -> Unit,
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .background(Color.White, shape = RoundedCornerShape(16.dp))
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Header Section
        Text(
            text = "Medication Card",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = patientName,
            style = MaterialTheme.typography.titleLarge.copy(color = Color.Red),
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
        Text(
            text = "Medications",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(medications) { medication ->
                MedicationItem(medication)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Schedule Section
        Text(
            text = "Schedule",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            schedule.forEach { entry ->
                ScheduleRow(entry)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun MedicationItem(medication: Medication) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.medication_24px),
            contentDescription = medication.name,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = medication.name,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = medication.dose,
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
                    painter = painterResource(id = R.drawable.medication_24px),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
