package com.example.gamezone.api

import com.example.gamezone.models.Game
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GameApi {
    // API Pública FreeToGame para cumplir requerimiento IE 3.1.4
    @GET("games")
    fun getGames(): Call<List<Game>>

    // Endpoint simulado para agregar juegos (Admin)
    @POST("games")
    fun addGame(@Body gameData: Map<String, String>): Call<ResponseBody>
}