package com.example.ParkinsonApp.Screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.example.ParkinsonApp.Authentication.LoginUIEvent
import com.example.ParkinsonApp.Authentication.LoginViewModel
import com.example.ParkinsonApp.R
import com.example.ParkinsonApp.UIComponents.ButtonComponent
import com.example.ParkinsonApp.UIComponents.ClickableLoginTextComponent
import com.example.ParkinsonApp.UIComponents.DividerTextComponent
import com.example.ParkinsonApp.UIComponents.HeadingTextComponent
import com.example.ParkinsonApp.UIComponents.MyTextFieldComponent
import com.example.ParkinsonApp.UIComponents.NormalTextComponent
import com.example.ParkinsonApp.UIComponents.PasswordTextFieldComponent
import com.example.ParkinsonApp.UIComponents.UnderLinedTextComponent


@Composable
fun LoginPage(
    loginViewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                NormalTextComponent("Welcome Back!")
                HeadingTextComponent("Login to your account")
                Spacer(modifier = Modifier.height(20.dp))

                MyTextFieldComponent(
                    labelValue = "Email",
                    painterResource = painterResource(id = R.drawable.ic_launcher_background),
                        onTextChanged ={ loginViewModel.onEvent(LoginUIEvent.EmailChanged(email))})

                PasswordTextFieldComponent(
                    labelValue = "Password",
                    painterResource = painterResource(id = R.drawable.ic_launcher_background),
                    { loginViewModel.onEvent(LoginUIEvent.EmailChanged(password))},
                    loginViewModel.loginUIState.value.passwordError

                )


                Spacer(modifier = Modifier.height(40.dp))
                UnderLinedTextComponent(value = "Forgot Password?")

                Spacer(modifier = Modifier.height(40.dp))

                ButtonComponent(
                    value = "Login",
                    onButtonClicked = {
                        loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                        onLoginSuccess()
                    },
                    isEnabled = loginViewModel.validationRules.value
                )

                Spacer(modifier = Modifier.height(20.dp))

                DividerTextComponent()

                ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {
                    onRegisterClick()
                })
            }

            if(loginViewModel.loginInProgress.value) {
                CircularProgressIndicator()
            }

        }

    }

}