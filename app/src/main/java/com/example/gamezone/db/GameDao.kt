package com.example.gamezone.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GameDao {
    @Insert
    suspend fun insertGame(game: LocalGame)

    @Update
    suspend fun updateGame(game: LocalGame)

    @Query("SELECT * FROM games ORDER BY id DESC")
    suspend fun getAllGames(): List<LocalGame>
    
    @Query("SELECT * FROM games WHERE id = :id LIMIT 1")
    suspend fun getGameById(id: Int): LocalGame?
}