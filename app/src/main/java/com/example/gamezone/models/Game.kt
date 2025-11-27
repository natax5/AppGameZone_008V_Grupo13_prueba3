package com.example.gamezone.models

import java.io.Serializable

data class Game(
    val id: Int,
    val title: String,
    val short_description: String,
    val thumbnail: String,
    val genre: String,
    val platform: String,
    // Campos nuevos para la vista de detalles
    val description: String? = null,
    val developer: String? = null,
    val release_date: String? = null
) : Serializable