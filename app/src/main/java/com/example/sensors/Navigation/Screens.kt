package com.example.sensors.Navigation

import kotlinx.serialization.Serializable


sealed class Screens(val route: String) {

    object Welcome : Screens("welcome")
    data class Login(val userType: String) : Screens("auth/login/$userType")
    data class SignUp(val userType: String) : Screens("auth/signup/$userType")
    object DoctorMain : Screens("Doctor")
    object PatientMain : Screens("Patient")
    object PatientDetail : Screens("patientDetail/{patientId}")
}