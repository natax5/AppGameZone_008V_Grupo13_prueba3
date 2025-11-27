package com.example.gamezone.models

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String,
    val user: UserData?
)

data class UserData(
    val id: Int,
    val fullName: String,
    val email: String,
    val phone: String? = null,
    val genres: String? = null
)

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String?,
    val genres: String
)

data class RegisterResponse(
    val message: String,
    val userId: Int?
)