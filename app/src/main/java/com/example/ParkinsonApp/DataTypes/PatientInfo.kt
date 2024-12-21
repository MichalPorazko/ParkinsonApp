package com.example.ParkinsonApp.DataTypes

data class PatientInfo(
    override val name: String,
    override val email: String,
    val doctorId: String,
    val password: String
) : UserInfo
