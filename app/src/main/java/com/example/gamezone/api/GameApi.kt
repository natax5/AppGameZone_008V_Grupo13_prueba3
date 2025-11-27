package com.example.gamezone.api

import com.example.gamezone.models.Game
import retrofit2.Call
import retrofit2.http.GET

interface GameApi {
    // API Pública FreeToGame para cumplir requerimiento IE 3.1.4
    @GET("games")
    fun getGames(): Call<List<Game>>
}