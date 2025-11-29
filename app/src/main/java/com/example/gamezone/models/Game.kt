package com.example.gamezone.models

import java.io.Serializable

data class Game(
    val id: Int,
    val title: String,
    val short_description: String,
    val thumbnail: String,
    val genre: String,
    val platform: String,
    val description: String? = null,
    val developer: String? = null,
    val release_date: String? = null,
    val price: Int = 0,
    val stock: Int = 0,
    val isLocal: Boolean = false // Para saber si es editable
) : Serializable