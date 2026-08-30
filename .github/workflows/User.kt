package com.zumar.app.model

data class User(
    val firstName: String,
    val middleName: String?,
    val lastName: String,
    val phone: String,
    val email: String,
    val password: String,
    val address: String,
    val state: String,
    val dob: String,
    val gender: String,
    val pin: String,
    var balance: Double = 0.0
)
