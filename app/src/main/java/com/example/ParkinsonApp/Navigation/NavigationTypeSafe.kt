package com.example.ParkinsonApp.Navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ParkinsonApp.Authentication.LoginViewModel
import com.example.ParkinsonApp.Authentication.SignUpViewModel
import com.example.ParkinsonApp.DataTypes.Medication
import com.example.ParkinsonApp.DataTypes.ScheduleEntry
import com.example.ParkinsonApp.Firebase.FirebaseRepository
import com.example.ParkinsonApp.Navigation.BottomNavigation.BottomNavItem
import com.example.ParkinsonApp.Navigation.BottomNavigation.BottomNavigationBar
import com.example.ParkinsonApp.R
import com.example.ParkinsonApp.Screens.Doctor.DoctorMainScreen
import com.example.ParkinsonApp.Screens.Authentication.LoginPage
import com.example.ParkinsonApp.Screens.Patient.PatientMedicationScreen
import com.example.ParkinsonApp.Screens.Patient.PatientMainScreen
import com.example.ParkinsonApp.Screens.Patient.PatientProfileScreen
import com.example.ParkinsonApp.Screens.Authentication.SignUpPage
import com.example.ParkinsonApp.Screens.Authentication.WelcomeScreen
import com.example.ParkinsonApp.Screens.Doctor.DoctorPatientListScreen
import com.example.ParkinsonApp.Screens.Doctor.DoctorProfileScreen
import com.example.ParkinsonApp.Screens.Doctor.PatientDetailsScreen

@Composable
fun NavigationTypeSafe() {
    val navController = rememberNavController()
    val firebaseRepository = FirebaseRepository()
    val loginViewModel: LoginViewModel = viewModel(factory = GenericViewModelFactory { LoginViewModel(firebaseRepository) })
    val sharedViewModel: SharedViewModel = viewModel(factory = GenericViewModelFactory { SharedViewModel(firebaseRepository) })

    Log.d("NavigationTypeSafe", "Navigation initialized.")

    fun NavGraphBuilder.authGraph(navController: NavHostController){
        navigation<SubGraph.Auth>(
            startDestination = NavRoute.Welcome
        ){

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
                val signupViewModel: SignUpViewModel = viewModel(factory = GenericViewModelFactory { SignUpViewModel(firebaseRepository, user) })
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

        }
    }

    fun NavGraphBuilder.doctorGraph(navController: NavHostController){

        navigation<SubGraph.DoctorDashboard>(
            startDestination = NavRoute.DoctorMain
        ){

            composable<NavRoute.DoctorMain> {
                Log.d("NavigationTypeSafe", "Navigated to doctorMainScreen")

            }

            composable<NavRoute.DoctorMain> {
                val currentRoute = NavRoute.DoctorMain
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = BottomNavItem.doctorItems,
                            currentRoute = currentRoute,
                            onItemClicked = { navRoute ->
                                navController.navigate(navRoute) {
                                    popUpTo(navController.graph.findStartDestination()) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    DoctorMainScreen(
                        sharedViewModel = sharedViewModel,
                        onAddPatientClick= { navController.navigate(NavRoute.DoctorPatients)},
                        onYourPatientsClick = { navController.navigate(NavRoute.DoctorPatients) },
                        onYourProfileClick = { navController.navigate(NavRoute.DoctorProfile) },
                        onRecentActionClicked = { patientId ->
                            navController.navigate("patientDetails/${patientId}")
                        },
                        paddingValues = innerPadding
                        )
                }
            }

            composable<NavRoute.DoctorPatients>{

                val currentRoute = NavRoute.DoctorMain
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = BottomNavItem.doctorItems,
                            currentRoute = currentRoute,
                            onItemClicked = { navRoute ->
                                navController.navigate(navRoute) {
                                    popUpTo(navController.graph.findStartDestination()) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    DoctorPatientListScreen(
                        sharedViewModel = sharedViewModel,
                        onPatientClicked = { patient ->
                            // Navigate to the detailed patient page
                            navController.navigate("patientDetails/${patient.id}")
                        },
                        innerPadding
                    )
                }
                }
            composable<NavRoute.DoctorProfile> {

                val currentRoute = NavRoute.DoctorMain
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = BottomNavItem.doctorItems,
                            currentRoute = currentRoute,
                            onItemClicked = { navRoute ->
                                navController.navigate(navRoute) {
                                    popUpTo(navController.graph.findStartDestination()) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                ) { innerPadding ->
                    Log.d("NavigationTypeSafe", "Navigated to doctorProfileScreen")
                    DoctorProfileScreen(innerPadding)
                }
                 }

            composable<NavRoute.PatientDetails> {
                PatientDetailsScreen(
                    patientId = it.toRoute<NavRoute.PatientDetails>().patientId,
                    sharedViewModel = sharedViewModel
                )

            }
        }



    }
    fun NavGraphBuilder.bottomNavComposable(
        route: NavRoute,
        items: List<BottomNavItem>,
        navController: NavHostController,
        content: @Composable (paddingValues: PaddingValues) -> Unit
    ) {
        composable(route.toString()) {
            Scaffold(
                bottomBar = {
                    BottomNavigationBar(
                        items = items,
                        currentRoute = route,
                        onItemClicked = { navRoute ->
                            navController.navigate(navRoute.toString()) {
                                popUpTo(navController.graph.findStartDestination()) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            ) { innerPadding ->
                content(innerPadding)
            }
        }
    }

    fun NavGraphBuilder.patientGraph(navController: NavHostController) {

        navigation<SubGraph.PatientDashboard>(
            startDestination = NavRoute.PatientMain,
        ) {

            bottomNavComposable(
                route = NavRoute.PatientMain,
                items = BottomNavItem.patientItems,
                navController = navController
            ) { innerPadding ->
                PatientMainScreen(
                    sharedViewModel = sharedViewModel,
                    onMedicationBoxClicked = { /* ... */ },
                    onMealBoxClicked = { /* ... */ },
                    onLogout = { navController.navigate(NavRoute.Welcome) },
                    innerPadding
                )
            }

            bottomNavComposable(
                route = NavRoute.PatientMedication,
                items = BottomNavItem.patientItems,
                navController = navController
            ) { innerPadding ->
                PatientMedicationScreen(
                    patientName = "Kyle",
                    yearBorn = 1983,
                    yearDiagnosed = 2005,
                    medications = listOf(
                        Medication("Madopar", "50mg/12.5mg", R.drawable.medication_24px),
                        Medication("Stalevo", "75mg/200mg", R.drawable.medication_24px),
                        Medication("Ibuprofen", "50mg", R.drawable.medication_24px),
                        Medication("Custom10", "10mg", R.drawable.medication_24px)
                    ),
                    schedule = listOf(
                        ScheduleEntry("07:00", listOf(Medication("Madopar", "50mg/12.5mg", R.drawable.medication_24px))),
                        ScheduleEntry("10:00", listOf(Medication("Stalevo", "75mg/200mg", R.drawable.medication_24px))),
                        ScheduleEntry("16:00", listOf(Medication("Ibuprofen", "50mg", R.drawable.medication_24px)))
                    ),
                    onShareClick = { navController.navigate(NavRoute.Welcome) },
                    innerPadding
                )
            }

            bottomNavComposable(
                route = NavRoute.PatientProfile,
                items = BottomNavItem.patientItems,
                navController = navController
            ) { innerPadding ->
                PatientProfileScreen(
                    sharedViewModel = sharedViewModel,
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
