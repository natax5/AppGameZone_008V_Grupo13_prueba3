package com.example.gamezone.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "games")
data class LocalGame(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val genre: String,
    val thumbnail: String,
    val short_description: String = "Juego agregado por administrador",
    val description: String = "Descripción detallada no disponible",
    val platform: String = "Multiplataforma",
    val developer: String = "Local",
    val release_date: String = "2024",
    val price: Int = 0,
    val stock: Int = 0
) : Serializable