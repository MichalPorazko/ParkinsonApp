package com.example.sensors.Navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.sensors.Authentication.GenericViewModelFactory
import com.example.sensors.Authentication.LoginViewModel
import com.example.sensors.Authentication.SignupViewModel
import com.example.sensors.Firebase.FirebaseRepository
import com.example.sensors.Screens.DoctorMainScreen
import com.example.sensors.Screens.LoginPage
import com.example.sensors.Screens.PatientMainScreen
import com.example.sensors.Screens.SignUpPage
import com.example.sensors.Screens.WelcomeScreen

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
