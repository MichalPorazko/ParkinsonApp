package com.example.ParkinsonApp.Screens.Patient

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.ParkinsonApp.Navigation.BottomNavigation.BottomNavItem
import com.example.ParkinsonApp.Navigation.BottomNavigation.BottomNavigationBar

@Composable
fun DoctorScaffold(
    navController: NavHostController,
    bottomNavItems: List<BottomNavItem>,
    content: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController, bottomNavItems)
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}