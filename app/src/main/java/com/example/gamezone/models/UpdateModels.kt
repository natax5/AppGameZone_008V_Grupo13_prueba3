package com.example.gamezone.models

data class UpdateUserRequest(
    val fullName: String,
    val email: String,
    val phone: String?,
    val genres: String
)

data class UpdateUserResponse(
    val message: String,
    val user: UserData?
)