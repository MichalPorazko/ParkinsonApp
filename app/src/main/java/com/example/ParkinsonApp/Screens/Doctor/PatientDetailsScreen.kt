package com.example.ParkinsonApp.Screens.Doctor

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import com.example.ParkinsonApp.ViewModels.DoctorViewModel

@Composable
fun PatientDetailsScreen(
    patientId: String,
    sharedViewModel: DoctorViewModel
) {
    // The sharedViewModel fetches patient details from your FirebaseRepository
    val patient by sharedViewModel.getPatientById(patientId).collectAsState()

    patient?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = it.data.firstName + " " + it.data.lastName , style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            // Medication Status
            Text(text = "Medication Status:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            // Display medication info or other data
        }
    } ?: run {
        // Handle the case where patient is null (not found or data not yet loaded)
        Text(
            text = "Patient not found",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}
