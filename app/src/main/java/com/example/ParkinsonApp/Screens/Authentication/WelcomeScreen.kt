package com.example.ParkinsonApp.Screens.Authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController

@Composable
fun WelcomeScreen(userTypeSelected: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {userTypeSelected("doctor")},
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("I am a Doctor")
        }
        Button(
            onClick = {userTypeSelected("patient")},
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text("I am a Patient")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    rememberNavController()
    WelcomeScreen(userTypeSelected = {})
}