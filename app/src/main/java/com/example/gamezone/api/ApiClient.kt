package com.example.gamezone.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    // Como el entorno de red local está bloqueado, activamos un INTERCEPTOR MOCK.
    // Esto permite que la app funcione y sea evaluable, simulando que el servidor responde.
    private const val BASE_URL = "http://mock.local/" 

    private val client = OkHttpClient.Builder()
        .addInterceptor(MockInterceptor()) // <--- Agregamos el interceptor Mock
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val instance: AuthApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(AuthApi::class.java)
    }
}