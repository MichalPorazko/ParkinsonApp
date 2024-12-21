package com.example.ParkinsonApp.Navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ParkinsonApp.Authentication.GenericViewModelFactory
import com.example.ParkinsonApp.Authentication.LoginViewModel
import com.example.ParkinsonApp.Authentication.SignupViewModel
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import com.example.ParkinsonApp.Screens.DoctorMainScreen
import com.example.ParkinsonApp.Screens.LoginPage
import com.example.ParkinsonApp.Screens.PatientMainScreen
import com.example.ParkinsonApp.Screens.SignUpPage
import com.example.ParkinsonApp.Screens.WelcomeScreen

@Composable
fun NavigationTypeSafe() {
    val navController = rememberNavController()
    val firebaseRepository = FirebaseRepository()
    val loginViewModel: LoginViewModel = viewModel(factory = GenericViewModelFactory { LoginViewModel(firebaseRepository) })
    val signupViewModel: SignupViewModel = viewModel(factory = GenericViewModelFactory { SignupViewModel(firebaseRepository) })
    val sharedViewModel: SharedViewModel = viewModel(factory = GenericViewModelFactory { SharedViewModel(firebaseRepository) })

    Log.d("NavigationTypeSafe", "Navigation initialized.")

    NavHost(navController = navController, startDestination = NavRoute.Welcome) {
        composable<NavRoute.Welcome> {
            Log.d("NavigationTypeSafe", "Navigated to Welcome screen.")
            WelcomeScreen(userTypeSelected = { userType ->
                Log.d("NavigationTypeSafe", "User type selected: $userType")
                navController.navigate(NavRoute.Login(userType))
            })
        }

        composable<NavRoute.Login> {
            val user = it.toRoute<NavRoute.Login>().userType
            Log.d("NavigationTypeSafe", "Navigated to Login screen for user: $user")
            LoginPage(
                loginViewModel,
                onLoginSuccess = {
                    Log.d("NavigationTypeSafe", "Login successful for user: $user")
                    when (user) {
                        "doctor" -> navController.navigate(NavRoute.DoctorMain)
                        "patient" -> navController.navigate(NavRoute.PatientMain)
                        else -> Log.e("NavigationTypeSafe", "Unknown user type: $user")
                    }
                },
                onRegisterClick = {
                    Log.d("NavigationTypeSafe", "Navigating to SignUp screen for user: $user")
                    navController.navigate(NavRoute.SignUp(user))
                }
            )
        }

        composable<NavRoute.SignUp> {
            val user = it.toRoute<NavRoute.SignUp>().userType
            Log.d("NavigationTypeSafe", "Navigated to SignUp screen for user: $user")
            SignUpPage(
                signupViewModel,
                signUp = {
                    Log.d("NavigationTypeSafe", "SignUp successful for user: $user")
                    when (user) {
                        "doctor" -> navController.navigate(NavRoute.DoctorMain)
                        "patient" -> navController.navigate(NavRoute.PatientMain)
                        else -> Log.e("NavigationTypeSafe", "Unknown user type: $user")
                    }
                },
                onLoginClick = {
                    Log.d("NavigationTypeSafe", "Navigating back to Login screen for user: $user")
                    when (user) {
                        "doctor" -> navController.navigate(NavRoute.DoctorMain)
                        "patient" -> navController.navigate(NavRoute.PatientMain)
                        else -> Log.e("NavigationTypeSafe", "Unknown user type: $user")
                    }
                }
            )
        }

        composable<NavRoute.DoctorMain> {
            Log.d("NavigationTypeSafe", "Navigated to doctorMainScreen")
            DoctorMainScreen(
                sharedViewModel = sharedViewModel,
                onLogout = {navController.navigate(NavRoute.Welcome) },
                onPatientSelected = {navController.navigate(it) },
                onRefresh = { sharedViewModel.refreshData() }
            )
        }

        composable<NavRoute.PatientMain> {
            Log.d("NavigationTypeSafe", "Navigated to PatientMainScreen")
            PatientMainScreen(
                sharedViewModel = sharedViewModel,
                onLogout = { navController.navigate(NavRoute.Welcome) }
            )
        }
    }
}
