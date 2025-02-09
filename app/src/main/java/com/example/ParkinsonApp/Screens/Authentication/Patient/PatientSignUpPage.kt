package com.example.ParkinsonApp.Screens.Authentication.Patient

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ParkinsonApp.Authentication.SignUpUIEvent
import com.example.ParkinsonApp.UIComponents.ButtonComponent
import com.example.ParkinsonApp.UIComponents.ClickableLoginTextComponent
import com.example.ParkinsonApp.UIComponents.DividerTextComponent
import com.example.ParkinsonApp.UIComponents.HeadingTextComponent
import com.example.ParkinsonApp.UIComponents.MyTextFieldComponent
import com.example.ParkinsonApp.UIComponents.NormalTextComponent
import com.example.ParkinsonApp.UIComponents.PasswordTextFieldComponent
import com.example.ParkinsonApp.ViewModels.SignUpViewModel

@Composable
fun PatientSignUpPage(signupViewModel: SignUpViewModel = viewModel(), signUp: () -> Unit, onLoginClick: () -> Unit) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(28.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {

                NormalTextComponent("Dzień Dobry")
                HeadingTextComponent("stwórz konto")
                Spacer(modifier = Modifier.height(20.dp))

                MyTextFieldComponent(
                    "Imię",
                    painterResource = painterResource(id = com.example.ParkinsonApp.R.drawable.ic_launcher_background),
                    onTextChanged = {
                        signupViewModel.onEvent(SignUpUIEvent.FirstNameChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.firstNameError
                )

                MyTextFieldComponent(
                    labelValue = "Nazwisko",
                    painterResource = painterResource(id = com.example.ParkinsonApp.R.drawable.ic_launcher_background),
                    onTextChanged = {
                        signupViewModel.onEvent(SignUpUIEvent.LastNameChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.lastNameError
                )

                MyTextFieldComponent(
                    "email",
                    painterResource = painterResource(id = com.example.ParkinsonApp.R.drawable.ic_launcher_background),
                    onTextChanged = {
                        signupViewModel.onEvent(SignUpUIEvent.EmailChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.emailError
                )

                PasswordTextFieldComponent(
                    "hasło",
                    painterResource = painterResource(id = com.example.ParkinsonApp.R.drawable.ic_launcher_background),
                    onTextSelected = {
                        signupViewModel.onEvent(SignUpUIEvent.PasswordChanged(it))
                    },
                    errorStatus = signupViewModel.registrationUIState.value.passwordError
                )

                Spacer(modifier = Modifier.height(40.dp))

                ButtonComponent(
                    "utwórz konto",
                    onButtonClicked = {
                        signupViewModel.onEvent(SignUpUIEvent.SignUpButtonClicked)
                        signUp()
                        Log.d(TAG, "the value is ${signupViewModel.allValidationsPassed}")
                    },
                    isEnabled = signupViewModel.allValidationsPassed.value
                )

                Spacer(modifier = Modifier.height(20.dp))

                DividerTextComponent()

                ClickableLoginTextComponent(tryingToLogin = true, onTextSelected = {
                    onLoginClick()
                })
            }

        }

        if(signupViewModel.signUpInProgress.value) {
            CircularProgressIndicator()
        }
    }


}
