package com.example.ParkinsonApp.Navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ParkinsonApp.ViewModels.LoginViewModel
import com.example.ParkinsonApp.ViewModels.SignUpViewModel
import com.example.ParkinsonApp.DataTypes.ScheduleEntry
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import com.example.ParkinsonApp.Navigation.BottomNavigation.BottomNavItem
import com.example.ParkinsonApp.Navigation.BottomNavigation.BottomNavigationBar
import com.example.ParkinsonApp.R
import com.example.ParkinsonApp.Screens.Authentication.LoginPage
import com.example.ParkinsonApp.Screens.Authentication.SignUpPage
import com.example.ParkinsonApp.Screens.Authentication.WelcomeScreen
import com.example.ParkinsonApp.Screens.Doctor.DoctorMainScreen
import com.example.ParkinsonApp.Screens.Doctor.DoctorPatientsListScreen
import com.example.ParkinsonApp.Screens.Doctor.DoctorProfileScreen
import com.example.ParkinsonApp.Screens.Doctor.PatientDetailsScreen
import com.example.ParkinsonApp.Screens.Patient.PatientMainScreen
import com.example.ParkinsonApp.Screens.Patient.PatientMedicationScreen
import com.example.ParkinsonApp.Screens.Patient.PatientProfileScreen
import com.example.ParkinsonApp.ViewModels.DoctorViewModel
import com.example.ParkinsonApp.ViewModels.PatientViewModel

@Composable
fun NavigationTypeSafe() {
    val navController = rememberNavController()
    val firebaseRepository = FirebaseRepository()
    val loginViewModel: LoginViewModel =
        viewModel(factory = GenericViewModelFactory { LoginViewModel(firebaseRepository) })
    val doctorViewModel: DoctorViewModel =
        viewModel(factory = GenericViewModelFactory { DoctorViewModel(firebaseRepository) })
    val patientViewModel: PatientViewModel =
        viewModel(factory = GenericViewModelFactory { PatientViewModel(firebaseRepository) })

    Log.d("NavigationTypeSafe", "Navigation initialized.")

    fun NavGraphBuilder.authGraph(navController: NavHostController) {
        navigation<SubGraph.Auth>(
            startDestination = NavRoute.Welcome
        ) {

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
                val signupViewModel: SignUpViewModel = viewModel(factory = GenericViewModelFactory {
                    SignUpViewModel(
                        firebaseRepository,
                        user
                    )
                })
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
                        Log.d(
                            "NavigationTypeSafe",
                            "Navigating back to Login screen for user: $user"
                        )
                        when (user) {
                            "doctor" -> navController.navigate(NavRoute.DoctorMain)
                            "patient" -> navController.navigate(NavRoute.PatientMain)
                            else -> Log.e("NavigationTypeSafe", "Unknown user type: $user")
                        }
                    }
                )
            }

        }
    }

    fun NavGraphBuilder.doctorGraph(navController: NavHostController) {
        navigation<SubGraph.DoctorDashboard>(
            startDestination = NavRoute.DoctorMain
        ) {

            bottomNavComposable<NavRoute.DoctorMain>(
                route = NavRoute.DoctorMain,
                items = BottomNavItem.doctorItems,
                navController = navController
            ) { innerPadding ->
                DoctorMainScreen(
                    doctorViewModel,
                    onAddPatientClick = { navController.navigate(NavRoute.DoctorPatients) },
                    onYourPatientsClick = { navController.navigate(NavRoute.DoctorPatients) },
                    onYourProfileClick = { navController.navigate(NavRoute.DoctorProfile) },
                    onRecentActionClicked = { patientId ->
                        navController.navigate(NavRoute.PatientDetails(patientId))
                    },
                    paddingValues = innerPadding
                )
            }

            bottomNavComposable<NavRoute.DoctorPatients>(
                route = NavRoute.DoctorPatients,
                items = BottomNavItem.doctorItems,
                navController = navController
            ) { innerPadding ->
                DoctorPatientsListScreen(
                    doctorViewModel,
                    onPatientClicked = { patientId ->
                        navController.navigate(NavRoute.PatientDetails(patientId))
                    },
                    innerPadding
                )
            }

            bottomNavComposable<NavRoute.DoctorProfile>(
                route = NavRoute.DoctorProfile,
                items = BottomNavItem.doctorItems,
                navController = navController
            ) { innerPadding ->
                Log.d("NavigationTypeSafe", "Navigated to doctorProfileScreen")
                DoctorProfileScreen(doctorViewModel, innerPadding, onLogout = { navController.navigate(NavRoute.Welcome) })
            }


            composable<NavRoute.PatientDetails> {
                PatientDetailsScreen(
                    patientId = it.toRoute<NavRoute.PatientDetails>().patientId,
                    doctorViewModel
                )

            }
        }
    }

    fun NavGraphBuilder.patientGraph(navController: NavHostController) {

        navigation<SubGraph.PatientDashboard>(
            startDestination = NavRoute.PatientMain,
        ) {

            bottomNavComposable<NavRoute.PatientMain>(
                route = NavRoute.PatientMain,
                items = BottomNavItem.patientItems,
                navController = navController
            ) { innerPadding ->
                PatientMainScreen(
                    patientViewModel,
                    onMedicationBoxClicked = { /* ... */ },
                    onMealBoxClicked = { /* ... */ },
                    onLogout = { navController.navigate(NavRoute.Welcome) },
                    innerPadding
                )
            }

            bottomNavComposable<NavRoute.PatientMedication>(
                route = NavRoute.PatientMedication,
                items = BottomNavItem.patientItems,
                navController = navController
            ) { innerPadding ->
                PatientMedicationScreen(
                    patientViewModel,
                    innerPadding,
                    onShareClick = { navController.navigate(NavRoute.Welcome) }
                )
            }

            bottomNavComposable<NavRoute.PatientProfile>(
                route = NavRoute.PatientProfile,
                items = BottomNavItem.patientItems,
                navController = navController
            ) { innerPadding ->
                PatientProfileScreen(
                    patientViewModel,
                    onLogout = { navController.navigate(NavRoute.Welcome) },
                    innerPadding
                )
            }

        }
    }

    NavHost(navController = navController, startDestination = SubGraph.Auth) {
        authGraph(navController)
        doctorGraph(navController)
        patientGraph(navController)
    }
}

inline fun <reified T : NavRoute> NavGraphBuilder.bottomNavComposable(
    route: NavRoute,
    items: List<BottomNavItem>,
    navController: NavHostController,
    crossinline content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    composable<T> {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    items = items,
                    currentRoute = route,
                    onItemClicked = { navRoute ->
                        navController.navigate(navRoute)
                    }
                )
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}
