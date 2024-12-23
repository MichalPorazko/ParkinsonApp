package com.example.ParkinsonApp.Navigation

import kotlinx.serialization.Serializable

sealed interface NavRoute {

    @Serializable
    object Welcome

    @Serializable
    data class Login(val userType: String)

    @Serializable
    data class SignUp(val userType: String)

    @Serializable
    object PatientMain

    @Serializable
    object PatientProfile

    @Serializable
    object PatientMedication

    @Serializable
    data object DoctorMain

    @Serializable
    data object DoctorPatients

    @Serializable
    data object DoctorProfile


}

sealed class SubGraph {

    @Serializable
    data object Auth : SubGraph()

    @Serializable
    data object DoctorDashboard : SubGraph()

    @Serializable
    data object PatientDashboard : SubGraph()

}