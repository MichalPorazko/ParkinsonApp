package com.example.ParkinsonApp.Screens.Authentication

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.ParkinsonApp.UIComponents.MyTextFieldComponent
import com.example.ParkinsonApp.UIComponents.PasswordTextFieldComponent

@Composable
fun BaseSignUpFields(
    firstName: String,
    onFirstNameChanged: (String) -> Unit,
    lastName: String,
    onLastNameChanged: (String) -> Unit,
    email: String,
    onEmailChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
// First Name
        MyTextFieldComponent(
            labelValue = "First Name",
            painterResource = painterResource(id = com.example.ParkinsonApp.R.drawable.ic_launcher_background),
            onTextChanged = onFirstNameChanged,
            errorStatus = false // or pass in your error-check logic
        )
// Last Name
        MyTextFieldComponent(
            labelValue = "Last Name",
            painterResource = painterResource(id = com.example.ParkinsonApp.R.drawable.ic_launcher_background),
            onTextChanged = onLastNameChanged,
            errorStatus = false
        )
// Email
        MyTextFieldComponent(
            labelValue = "Email",
            painterResource = painterResource(id = com.example.ParkinsonApp.R.drawable.ic_launcher_background),
            onTextChanged = onEmailChanged,
            errorStatus = false
        )
// Password
        PasswordTextFieldComponent(
            labelValue = "Password",
            painterResource = painterResource(id = com.example.ParkinsonApp.R.drawable.ic_launcher_background),
            onTextSelected = onPasswordChanged,
            errorStatus = false
        )
    }
}