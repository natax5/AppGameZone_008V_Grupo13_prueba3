package com.example.gamezone

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gamezone.db.AppDatabase
import com.example.gamezone.db.LocalGame
import kotlinx.coroutines.launch

class AddGameActivity : AppCompatActivity() {
    
    private var isEditMode = false
    private var gameIdToEdit = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game)

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val etName = findViewById<EditText>(R.id.etGameName)
        val spinnerGenre = findViewById<Spinner>(R.id.spinnerGameGenre)
        val etPrice = findViewById<EditText>(R.id.etGamePrice)
        val etStock = findViewById<EditText>(R.id.etGameStock)
        val etImage = findViewById<EditText>(R.id.etGameImageUrl)
        val btnSave = findViewById<Button>(R.id.btnSaveGame)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        // Configurar Spinner
        val genres = listOf(
            getString(R.string.genre_fiction),
            getString(R.string.genre_non_fiction),
            getString(R.string.genre_mystery),
            getString(R.string.genre_horror),
            getString(R.string.genre_suspense),
            getString(R.string.genre_history)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genres)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGenre.adapter = adapter

        // Verificar si estamos en modo edición
        if (intent.hasExtra("EDIT_GAME_ID")) {
            isEditMode = true
            gameIdToEdit = intent.getIntExtra("EDIT_GAME_ID", -1)
            tvTitle.text = "Editar Juego"
            btnSave.text = "Actualizar Juego"
            loadGameData(gameIdToEdit, etName, spinnerGenre, etPrice, etStock, etImage, genres)
        }

        btnCancel.setOnClickListener { finish() }

        btnSave.setOnClickListener {
            val name = etName.text.toString().trim()
            val selectedGenre = spinnerGenre.selectedItem.toString()
            val imageUrl = etImage.text.toString().trim()
            val priceStr = etPrice.text.toString().trim()
            val stockStr = etStock.text.toString().trim()

            if (name.isEmpty() || imageUrl.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            val price = priceStr.toIntOrNull() ?: 0
            val stock = stockStr.toIntOrNull() ?: 0

            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                
                if (isEditMode) {
                    // Actualizar
                    val updatedGame = LocalGame(
                        id = gameIdToEdit,
                        title = name,
                        genre = selectedGenre,
                        thumbnail = imageUrl,
                        price = price,
                        stock = stock
                    )
                    db.gameDao().updateGame(updatedGame)
                    Toast.makeText(this@AddGameActivity, "Juego actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    // Insertar Nuevo
                    val newGame = LocalGame(
                        title = name,
                        genre = selectedGenre,
                        thumbnail = imageUrl,
                        price = price,
                        stock = stock
                    )
                    db.gameDao().insertGame(newGame)
                    Toast.makeText(this@AddGameActivity, "Juego guardado", Toast.LENGTH_SHORT).show()
                }
                finish()
            }
        }
    }

    private fun loadGameData(id: Int, etName: EditText, spinner: Spinner, etPrice: EditText, etStock: EditText, etImage: EditText, genres: List<String>) {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val game = db.gameDao().getGameById(id)
            
            if (game != null) {
                etName.setText(game.title)
                etPrice.setText(game.price.toString())
                etStock.setText(game.stock.toString())
                etImage.setText(game.thumbnail)
                
                val genreIndex = genres.indexOf(game.genre)
                if (genreIndex >= 0) {
                    spinner.setSelection(genreIndex)
                }
            }
        }
    }
}