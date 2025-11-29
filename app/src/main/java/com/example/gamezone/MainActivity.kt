package com.example.gamezone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamezone.api.RetrofitClient
import com.example.gamezone.db.AppDatabase
import com.example.gamezone.models.Game
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var rvGames: RecyclerView
    private lateinit var btnProfile: Button
    private lateinit var btnBack: Button
    private lateinit var adminPanelContainer: View
    private lateinit var btnAdminPanel: Button
    private lateinit var fabCart: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvGames = findViewById(R.id.rvGames)
        btnProfile = findViewById(R.id.btnProfile)
        btnBack = findViewById(R.id.btnBack)
        adminPanelContainer = findViewById(R.id.adminPanelContainer)
        btnAdminPanel = findViewById(R.id.btnAdminPanel)
        fabCart = findViewById(R.id.fabCart)
        
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
        
        fabCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadAllGames()
    }

    private fun loadAllGames() {
        val prefs = getSharedPreferences("GameZonePrefs", Context.MODE_PRIVATE)
        val userGenres = prefs.getString("USER_GENRES", "") ?: ""
        val genreList = userGenres.split(",").map { it.trim().lowercase() }
        
        // 1. Cargar juegos locales desde Room
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val localGames = db.gameDao().getAllGames().map { local ->
                // Convertir LocalGame a Game (modelo usado por el adapter)
                Game(
                    id = local.id + 10000, // Offset para evitar colisión de IDs con API
                    title = local.title,
                    short_description = local.short_description,
                    thumbnail = local.thumbnail,
                    genre = local.genre,
                    platform = local.platform,
                    description = local.description,
                    developer = local.developer,
                    release_date = local.release_date,
                    price = local.price,
                    stock = local.stock,
                    isLocal = true // Marcamos como local para habilitar edición
                )
            }

            // 2. Cargar juegos remotos desde Retrofit API
            RetrofitClient.instance.getGames().enqueue(object : Callback<List<Game>> {
                override fun onResponse(call: Call<List<Game>>, response: Response<List<Game>>) {
                    val apiGames = if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
                    
                    // 3. Combinar listas (Locales primero)
                    val allGames = localGames + apiGames
                    
                    // 4. Filtrar por gustos
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
                            
                            // Si el juego es local y coincide parcialmente con el texto del género
                            if (genreList.any { userGenre -> gameGenre.contains(userGenre) }) match = true

                            match
                        }
                    } else {
                        allGames
                    }

                    val finalGames = if (filteredGames.isNotEmpty()) filteredGames else allGames
                    
                    rvGames.adapter = GamesAdapter(finalGames)
                }

                override fun onFailure(call: Call<List<Game>>, t: Throwable) {
                    // Si falla la API, al menos mostrar los locales
                    if (localGames.isNotEmpty()) {
                        rvGames.adapter = GamesAdapter(localGames)
                        Toast.makeText(applicationContext, "Sin conexión. Mostrando juegos locales.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
    }
}