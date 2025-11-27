package com.example.gamezone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamezone.api.RetrofitClient
import com.example.gamezone.models.Game
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var rvGames: RecyclerView
    private lateinit var btnProfile: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvGames = findViewById(R.id.rvGames)
        btnProfile = findViewById(R.id.btnProfile)
        btnBack = findViewById(R.id.btnBack)
        
        rvGames.layoutManager = LinearLayoutManager(this)

        btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        btnBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish() 
        }
    }

    override fun onResume() {
        super.onResume()
        loadGames()
    }

    private fun loadGames() {
        val prefs = getSharedPreferences("GameZonePrefs", Context.MODE_PRIVATE)
        val userGenres = prefs.getString("USER_GENRES", "") ?: ""
        val genreList = userGenres.split(",").map { it.trim().lowercase() }

        RetrofitClient.instance.getGames().enqueue(object : Callback<List<Game>> {
            override fun onResponse(call: Call<List<Game>>, response: Response<List<Game>>) {
                if (response.isSuccessful) {
                    val allGames = response.body() ?: emptyList()
                    
                    val filteredGames = if (userGenres.isNotEmpty()) {
                        allGames.filter { game ->
                            val gameGenre = game.genre.lowercase()
                            var match = false
                            
                            // Mapeo de "Géneros de Libros" (Registro) a "Géneros de Juegos" (Mock API)
                            
                            // Ficción -> Fantasy, Sci-Fi
                            if (genreList.contains("ficción") && (gameGenre.contains("fantasy") || gameGenre.contains("sci-fi"))) match = true
                            
                            // Terror -> Horror, Zombie
                            if (genreList.contains("terror") && (gameGenre.contains("horror") || gameGenre.contains("zombie"))) match = true
                            
                            // Misterio -> Mystery, Adventure, Strategy
                            if (genreList.contains("misterio") && (gameGenre.contains("mystery") || gameGenre.contains("adventure") || gameGenre.contains("strategy"))) match = true
                            
                            // Suspenso -> Card Game (Estrategia tensa), Mystery
                            if (genreList.contains("suspenso") && (gameGenre.contains("card") || gameGenre.contains("mystery"))) match = true
                            
                            // Historia -> Historical, Strategy
                            if (genreList.contains("historia") && (gameGenre.contains("historical") || gameGenre.contains("strategy"))) match = true
                            
                            // No Ficción -> Simulation, Sports, Racing (Cosas reales)
                            if (genreList.contains("no ficción") && (gameGenre.contains("simulation") || gameGenre.contains("sports") || gameGenre.contains("racing"))) match = true

                            match
                        }
                    } else {
                        allGames
                    }

                    val finalGames = if (filteredGames.isNotEmpty()) filteredGames else allGames
                    val message = if (filteredGames.isNotEmpty()) "Filtrado por tus gustos ($userGenres)" else "Mostrando todos los juegos (Sin coincidencias exactas)"

                    rvGames.adapter = GamesAdapter(finalGames)
                    
                    if (userGenres.isNotEmpty()) {
                        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    }
                    
                } else {
                    Toast.makeText(applicationContext, "Error al cargar juegos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Game>>, t: Throwable) {
                Toast.makeText(applicationContext, "Fallo de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}