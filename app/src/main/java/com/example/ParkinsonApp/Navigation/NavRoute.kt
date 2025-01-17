// NavRoute.kt

package com.example.ParkinsonApp.Navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute {

    @Serializable
    data object Welcome: NavRoute()

    @Serializable
    data class Login(val userType: String): NavRoute()

    @Serializable
    data class SignUp(val userType: String): NavRoute()

    @Serializable
    data object PatientMain: NavRoute()

    @Serializable
    data object PatientProfile: NavRoute()

    @Serializable
    data object PatientMedication: NavRoute()

    @Serializable
    data object DoctorMain: NavRoute()

    @Serializable
    data object DoctorPatients: NavRoute()

    @Serializable
    data object DoctorProfile: NavRoute()

    @Serializable
    data class PatientDetails(val patientId: String): NavRoute()


}

@Serializable
sealed class SubGraph {

    @Serializable
    data object Auth : SubGraph()

    @Serializable
    data object DoctorDashboard : SubGraph()

    @Serializable
    data object PatientDashboard : SubGraph()
}