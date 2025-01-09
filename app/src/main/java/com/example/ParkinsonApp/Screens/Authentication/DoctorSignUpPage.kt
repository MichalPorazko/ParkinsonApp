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
fun DoctorSignUpPage(
// You can either store data in a separate view model or pass in a doc object
    doctorData: UserType.DoctorUser,
    onDoctorDataChanged: (UserType.DoctorUser) -> Unit,
    onSignUpClicked: () -> Unit,
    onLoginClick: () -> Unit
) {
    /*Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp)
    ) {
        Text("Doctor Signup", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
// Shared fields
        BaseSignUpFields(
            firstName = doctorData.firstName,
            onFirstNameChanged = { newValue ->
                onDoctorDataChanged(doctorData.copy(firstName = newValue))
            },
            lastName = doctorData.lastName,
            onLastNameChanged = { newValue ->
                onDoctorDataChanged(doctorData.copy(lastName = newValue))
            },
            email = doctorData.email,
            onEmailChanged = { newValue ->
                onDoctorDataChanged(doctorData.copy(email = newValue))
            },
            password = doctorData.password,
            onPasswordChanged = { newValue ->
                onDoctorDataChanged(doctorData.copy(password = newValue))
            }
        )
        Spacer(Modifier.height(16.dp))
// Doctor-specific fields
        MyTextFieldComponent(
            labelValue = "Specialization",
            painterResource = painterResource(id = com.example.ParkinsonApp.R.drawable.ic_launcher_background),
            onTextChanged = { newValue ->
                onDoctorDataChanged(doctorData.copy(specialization = newValue))
            },
            errorStatus = false
        )
        Spacer(Modifier.height(16.dp))
// Buttons
        ButtonComponent(
            "Sign Up as Doctor",
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