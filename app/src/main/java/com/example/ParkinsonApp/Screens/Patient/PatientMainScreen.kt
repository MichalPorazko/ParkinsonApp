package com.example.ParkinsonApp.Screens.Patient

import androidx.compose.runtime.Composable
import com.example.ParkinsonApp.Navigation.SharedViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.ParkinsonApp.Firebase.FirebaseRepository

@Composable
fun PatientMainScreen(
    sharedViewModel: SharedViewModel,
    //onMeasurementSelected: (String) -> Unit, // Callback when a measurement is selected
    onLogout: () -> Unit // Callback for logging out
) {
    // Sample data for past measurements (in a real app, fetch from ViewModel/Database)
    val pastMeasurements = listOf(
        "Blood Pressure: 120/80 mmHg",
        "Dialysis Status: Normal",
        "Heart Rate: 72 bpm"
    )

    // UI Layout
    Scaffold(
        topBar = {
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Welcome Section
                Text(
                    text = "Welcome, Patient!",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                // Current Health Measurements
                //MeasurementOptionsSection(onMeasurementSelected)

                // Past Measurements
                PastMeasurementsSection(pastMeasurements)
            }
        }
    )
}

// Measurement Options Section
@Composable
fun MeasurementOptionsSection(onMeasurementSelected: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Perform Health Measurements",
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = { onMeasurementSelected("Blood Pressure") }) {
                Text(text = "Blood Pressure")
            }
            Button(onClick = { onMeasurementSelected("Dialysis Machine") }) {
                Text(text = "Dialysis Machine")
            }
            Button(onClick = { onMeasurementSelected("Heart Rate") }) {
                Text(text = "Heart Rate")
            }
        }
    }
}

// Past Measurements Section
@Composable
fun PastMeasurementsSection(measurements: List<String>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Past Measurements",
            style = MaterialTheme.typography.titleLarge
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(measurements) { measurement ->
                MeasurementCard(measurement)
            }
        }
    }
}

// Measurement Card Composable
@Composable
fun MeasurementCard(measurement: String) {
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
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Measurement Icon",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = measurement,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPatientMainScreen() {
    val sharedViewModel = SharedViewModel(firebaseRepository = FirebaseRepository())
    PatientMainScreen(
        sharedViewModel = sharedViewModel,
        onLogout = { /* Handle logout */ }
    )
}