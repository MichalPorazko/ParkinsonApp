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
    object DoctorMain

    @Serializable
    object PatientMain


}