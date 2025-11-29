package com.example.gamezone.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: CartItem)

    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    suspend fun getCartItems(userId: Int): List<CartItem>

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: Int)

    @Delete
    suspend fun deleteItem(item: CartItem)
    
    // Verificar si ya existe un item igual para sumar cantidad (opcional, por simplicidad agregaremos filas nuevas o actualizaremos manualmente)
    @Query("SELECT * FROM cart_items WHERE userId = :userId AND gameId = :gameId LIMIT 1")
    suspend fun getItemByGameId(userId: Int, gameId: Int): CartItem?
    
    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :id")
    suspend fun updateQuantity(id: Int, quantity: Int)
}