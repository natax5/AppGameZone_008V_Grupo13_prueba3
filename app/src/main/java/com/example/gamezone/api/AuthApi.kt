package com.example.gamezone.api

import com.example.gamezone.models.LoginRequest
import com.example.gamezone.models.LoginResponse
import com.example.gamezone.models.RegisterRequest
import com.example.gamezone.models.RegisterResponse
import com.example.gamezone.models.UpdateUserRequest
import com.example.gamezone.models.UpdateUserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AuthApi {
    @POST("login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>

    @PUT("users/{id}")
    fun updateUser(@Path("id") userId: Int, @Body request: UpdateUserRequest): Call<UpdateUserResponse>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") userId: Int): Call<Void>
}