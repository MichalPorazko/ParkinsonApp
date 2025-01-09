package com.example.ParkinsonApp.Screens.Authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ParkinsonApp.DataTypes.UserType
import com.example.ParkinsonApp.UIComponents.ButtonComponent
import com.example.ParkinsonApp.UIComponents.ClickableLoginTextComponent
import com.example.ParkinsonApp.UIComponents.MyTextFieldComponent

@Composable
fun PatientSignUpPage(
    patientData: UserType.PatientUser,
    onPatientDataChanged: (UserType.PatientUser) -> Unit,
    onSignUpClicked: () -> Unit,
    onLoginClick: () -> Unit
) {
  /*  Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp)
    ) {
        Text("Patient Signup", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
// Shared fields
        BaseSignUpFields(
            firstName = patientData.firstName,
            onFirstNameChanged = { newValue ->
                onPatientDataChanged(patientData.copy(firstName = newValue))
            },
            lastName = patientData.lastName,
            onLastNameChanged = { newValue ->
                onPatientDataChanged(patientData.copy(lastName = newValue))
            },
            email = patientData.email,
            onEmailChanged = { newValue ->
                onPatientDataChanged(patientData.copy(email = newValue))
            },
            password = patientData.password,
            onPasswordChanged = { newValue ->
                onPatientDataChanged(patientData.copy(password = newValue))
            }
        )
        Spacer(Modifier.height(16.dp))
// Patient-specific fields
        MyTextFieldComponent(
            labelValue = "Diagnosis",
            painterResource = painterResource(id = com.example.ParkinsonApp.R.drawable.ic_launcher_background),
            onTextChanged = { newValue ->
                onPatientDataChanged(patientData.copy(diagnosis = newValue))
            },
            errorStatus = false
        )
        Spacer(Modifier.height(16.dp))
// Buttons
        ButtonComponent(
            "Sign Up as Patient",
            onButtonClicked = { onSignUpClicked() },
            isEnabled = true
        )
        Spacer(Modifier.height(16.dp))
        ClickableLoginTextComponent(
            tryingToLogin = true,
            onTextSelected = { onLoginClick() }
        )
    }*/
}