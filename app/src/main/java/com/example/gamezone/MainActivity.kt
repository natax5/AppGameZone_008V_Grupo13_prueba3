package com.example.gamezone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
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
    private lateinit var adminPanelContainer: View
    private lateinit var btnAdminPanel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvGames = findViewById(R.id.rvGames)
        btnProfile = findViewById(R.id.btnProfile)
        btnBack = findViewById(R.id.btnBack)
        adminPanelContainer = findViewById(R.id.adminPanelContainer)
        btnAdminPanel = findViewById(R.id.btnAdminPanel)
        
        rvGames.layoutManager = LinearLayoutManager(this)

        // Verificar Rol de Admin
        val prefs = getSharedPreferences("GameZonePrefs", Context.MODE_PRIVATE)
        val isAdmin = prefs.getBoolean("IS_ADMIN", false)

        if (isAdmin) {
            adminPanelContainer.visibility = View.VISIBLE
            btnAdminPanel.setOnClickListener {
                startActivity(Intent(this, AdminActivity::class.java))
            }
        } else {
            adminPanelContainer.visibility = View.GONE
        }

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
                            
                            if (genreList.contains("ficción") && (gameGenre.contains("fantasy") || gameGenre.contains("sci-fi"))) match = true
                            if (genreList.contains("terror") && (gameGenre.contains("horror") || gameGenre.contains("zombie"))) match = true
                            if (genreList.contains("misterio") && (gameGenre.contains("mystery") || gameGenre.contains("adventure") || gameGenre.contains("strategy"))) match = true
                            if (genreList.contains("suspenso") && (gameGenre.contains("card") || gameGenre.contains("mystery"))) match = true
                            if (genreList.contains("historia") && (gameGenre.contains("historical") || gameGenre.contains("strategy"))) match = true
                            if (genreList.contains("no ficción") && (gameGenre.contains("simulation") || gameGenre.contains("sports") || gameGenre.contains("racing"))) match = true

                            match
                        }
                    } else {
                        allGames
                    }

                    val finalGames = if (filteredGames.isNotEmpty()) filteredGames else allGames
                    val message = if (filteredGames.isNotEmpty()) "Filtrado por tus gustos ($userGenres)" else "Mostrando todos los juegos"

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