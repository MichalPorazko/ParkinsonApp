package com.example.ParkinsonApp.Navigation.BottomNavigation

import com.example.ParkinsonApp.Navigation.NavRoute
import com.example.ParkinsonApp.R
import kotlinx.serialization.Serializable

@Serializable
sealed class BottomNavItem(val route: NavRoute, val icon: Int, val name: String) {

    @Serializable
    data object PMain : BottomNavItem(
        route = NavRoute.PatientMain,
        icon = R.drawable.home_24px,
        name = "PatientHome"
    )

    @Serializable
    data object PProfile : BottomNavItem(
        route = NavRoute.PatientProfile,
        icon = R.drawable.medication_24px,
        name = "PatientProfile"
    )

    @Serializable
    data object PMedication : BottomNavItem(
        route = NavRoute.PatientMedication,
        icon = R.drawable.medication_24px,
        name = "PatientMedication"
    )

    // Doctor Bottom Navigation Items
    @Serializable
    data object DMain : BottomNavItem(
        route = NavRoute.DoctorMain,
        icon = R.drawable.home_24px,
        name = "Home"
    )

    @Serializable
    data object DPatients : BottomNavItem(
        route = NavRoute.DoctorPatients,
        icon = R.drawable.patient_list_24px,
        name = "Patients"
    )

    @Serializable
    data object DProfile : BottomNavItem(
        route = NavRoute.DoctorProfile,
        icon = R.drawable.stethoscope_24px,
        name = "Profile"
    )

    companion object {
        val patientItems = listOf(PMain, PMedication, PProfile)
        val doctorItems = listOf(DMain, DPatients, DProfile)
    }
}

