package com.example.ParkinsonApp.Screens.Doctor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ParkinsonApp.DataTypes.*
import com.example.ParkinsonApp.ViewModels.DoctorViewModel

@Composable
fun DoctorProfileScreen(
    doctorViewModel: DoctorViewModel,
    paddingValues: PaddingValues,
    onLogout: () -> Unit
) {
    val doctorData by doctorViewModel.doctorData.collectAsState()

    val doctorName = "${doctorData?.data?.firstName ?: ""} ${doctorData?.data?.lastName ?: ""}"
    val pwzNumber = doctorData?.data?.pwzNumber ?: ""
    val patients = doctorData?.data?.patients ?: listOf()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Doctor Profile",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Doctor Details
        ProfileItem(label = "Name", value = doctorName)
        ProfileItem(label = "PWZ Number", value = pwzNumber.toString())

        Spacer(modifier = Modifier.height(16.dp))

        // Patients List
        Text(
            text = "Patients",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(patients.size) { index ->
                val patient = patients[index]
                PatientItem(patient)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logout Button
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("Logout")
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun PatientItem(patient: PatientData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFEFEFEF), shape = MaterialTheme.shapes.small)
            .padding(12.dp)
    ) {
        Text(
            text = "${patient.firstName} ${patient.lastName}",
            style = MaterialTheme.typography.bodyLarge
        )
        // Add more details if needed
    }
}
