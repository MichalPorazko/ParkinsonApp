package com.example.sensors.Screens

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.sensors.Navigation.SharedViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.sensors.Firebase.FirebaseRepository

@Composable
fun DoctorMainScreen(
    sharedViewModel: SharedViewModel,
    onLogout: () -> Unit,
    onPatientSelected: (String) -> Unit,
    onRefresh: () -> Unit
) {
    // A Scaffold to provide a top bar, potential floating action buttons, and a content area.
    Scaffold(
        content = { paddingValues ->
            // Content area for patient list and other UI elements
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Toolbar(
                    onAddPatient = { /* Add Patient Action */ },
                    onFilterPatients = { /* Filter Action */ },
                    onRefresh = onRefresh // Reuse the existing refresh callback
                )

                AnnouncementsSection(
                    announcements = listOf(
                        "Critical Alert: Patient A requires immediate attention.",
                        "Reminder: Update patient files before end of the week.",
                        "System downtime scheduled for 3 AM tomorrow."
                    )
                )
                // Placeholder for refresh button or indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = onRefresh) {
                        Text("Refresh")
                    }
                }

                StatisticsSection(
                    statistics = mapOf(
                        "Total Patients" to "15",
                        "Critical Patients" to "3",
                        "Average Dialysis Sessions" to "4/week"
                    )
                )

                // Placeholder for a patient list or other relevant information
                // We'll refine this in the next part
                Text(text = "List of Patients will appear here")

                PatientList(
                    patients = listOf("Patient A", "Patient B", "Patient C"),
                    onPatientSelected = onPatientSelected
                )
            }
        }
    )
}

@Composable
fun PatientList(
    patients: List<String>, // Replace String with your PatientInfo class if available
    onPatientSelected: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(patients) { patient ->
            PatientCard(patientName = patient, onClick = { onPatientSelected(patient) })
        }
    }
}

@Composable
fun PatientCard(
    patientName: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = patientName,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Button(onClick = onClick) {
                Text(text = "View")
            }
        }
    }
}

@Composable
fun AnnouncementsSection(
    announcements: List<String> // List of announcements/messages
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Announcements",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(announcements) { announcement ->
                AnnouncementCard(announcement = announcement)
            }
        }
    }
}

@Composable
fun AnnouncementCard(announcement: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = announcement,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    }


}

@Composable
fun StatisticsSection(
    statistics: Map<String, String> // Key-value pairs for stats (e.g., "Critical Patients" -> "5")
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Statistics",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(statistics.entries.toList()) { (key, value) ->
                StatisticCard(statTitle = key, statValue = value)
            }
        }
    }
}

@Composable
fun StatisticCard(
    statTitle: String,
    statValue: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = statTitle,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = statValue,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun Toolbar(
    onAddPatient: () -> Unit,
    onFilterPatients: () -> Unit,
    onRefresh: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(onClick = onAddPatient) {
            Text(text = "Add Patient")
        }
        Button(onClick = onFilterPatients) {
            Text(text = "Filter")
        }
        Button(onClick = onRefresh) {
            Text(text = "Refresh")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDoctorMainScreen() {
    val sharedViewModel = SharedViewModel(firebaseRepository = FirebaseRepository())
    DoctorMainScreen(
        sharedViewModel = sharedViewModel,
        onLogout = { /* Handle logout action */ },
        onPatientSelected = { /* Handle patient selection */ },
        onRefresh = { /* Handle refresh action */ }
    )
}


