package com.example.ParkinsonApp.Screens.Doctor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ParkinsonApp.DataTypes.Patient
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import com.example.ParkinsonApp.Navigation.SharedViewModel

@Composable
fun DoctorPatientListScreen(
    sharedViewModel: SharedViewModel,
    onPatientClicked: (Patient) -> Unit
) {
    val patients by sharedViewModel.patients.collectAsState(initial = emptyList())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(patients) { patient ->
            ExpandablePatientCard(
                patient = patient,
                onPatientClicked = { onPatientClicked(patient) }
            )
        }
    }
}

@Composable
fun ExpandablePatientCard(
    patient: Patient,
    onPatientClicked: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPatientClicked() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = patient.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.MoreVert else Icons.Default.Add,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }
            // Expandable Content
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Age: ${patient.age}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDoctorPatientListScreen() {
    val sharedViewModel = SharedViewModel(firebaseRepository = FirebaseRepository())
    DoctorPatientListScreen(
        sharedViewModel = sharedViewModel,
        onPatientClicked = { /* Handle patient selection */ }
    )
}