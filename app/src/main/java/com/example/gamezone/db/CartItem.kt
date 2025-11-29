package com.example.gamezone.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,        // Para saber de quién es el carrito
    val gameId: Int,
    val title: String,
    val price: Int,
    val quantity: Int,
    val thumbnail: String,
    val isLocalGame: Boolean // Para saber si debemos descontar stock de la BD local al comprar
)