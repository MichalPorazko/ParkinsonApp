package com.example.sensors.DataTypes

data class DoctorInfo(
    override val name: String,
    override val email: String,
    val authenticationNumber: String
) : UserInfo