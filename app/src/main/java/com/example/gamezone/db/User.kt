package com.example.gamezone.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fullName: String,
    val email: String,
    val password: String, // En producción esto debería ir hasheado
    val phone: String?,
    val genres: String // Guardaremos los géneros separados por coma
)