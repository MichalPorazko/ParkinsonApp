package com.example.ParkinsonApp.Screens.Patient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.ParkinsonApp.DataTypes.Emotion
import com.example.ParkinsonApp.DataTypes.MedicationSchedule
import com.example.ParkinsonApp.DataTypes.Water
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import com.example.ParkinsonApp.ViewModels.PatientViewModel


//data class PatientData(
//    val firstName: String = "",
//    val lastName: String = "",
//    var medications: MedicationSchedule = MedicationSchedule(),
//    var waterIntake: Water = Water(),
//    var emotions: List<Emotion> = emptyList()
//){

@Composable
fun PatientProfileScreen(
    sharedViewModel: PatientViewModel,
    onLogout: () -> Unit,
    paddingValues: PaddingValues
) {
    val patientData by sharedViewModel.patientData.collectAsState()

    val patientName = "${patientData?.firstName ?: ""} ${patientData?.lastName ?: ""}"
    val medicationSchedule = patientData?.medications ?: MedicationSchedule()
    val waterIntake = patientData?.waterIntake ?: Water()
    val emotions = patientData?.emotions ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Profile",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Patient Details
        ProfileItem(label = "Name", value = patientName)
        ProfileItem(label = "Email", value = medicationSchedule.medications.toString())
        ProfileItem(label = "Doctor", value = waterIntake.toString())
        ProfileItem(label = "Emotions", value = emotions.toString())

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
