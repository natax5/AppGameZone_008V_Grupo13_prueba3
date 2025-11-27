package com.example.gamezone.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://www.freetogame.com/api/"

    // Usamos un Mock Interceptor para expandir la base de datos de juegos
    // ya que la API real a veces no devuelve suficientes ejemplos para todos los géneros
    private val client = OkHttpClient.Builder()
        .addInterceptor(MockGameInterceptor()) 
        .build()

    val instance: GameApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(GameApi::class.java)
    }
}