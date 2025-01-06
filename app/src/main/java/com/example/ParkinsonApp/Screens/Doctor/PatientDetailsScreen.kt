package com.example.ParkinsonApp.Screens.Doctor

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import com.example.ParkinsonApp.Navigation.SharedViewModel

@Composable
fun PatientDetailsScreen(
    patientId: String,
    sharedViewModel: SharedViewModel
) {
    val patient by sharedViewModel.getPatientById(patientId).collectAsState(initial = null)

    patient?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = it.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Age: ${it.age}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            // Medication Status
            Text(text = "Medication Status:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            // Display medication details
            // You can create a list or any other UI to represent medication data
        }
    } ?: run {
        // Handle null case (patient not found)
        Text(text = "Patient not found", modifier = Modifier.fillMaxSize(), style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPatientDetailsScreen() {
    val sharedViewModel = SharedViewModel(firebaseRepository = FirebaseRepository())
    PatientDetailsScreen(
        patientId = "patientId",
        sharedViewModel = sharedViewModel
    )
}