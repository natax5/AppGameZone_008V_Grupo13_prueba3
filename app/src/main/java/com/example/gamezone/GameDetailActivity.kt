package com.example.gamezone

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gamezone.models.Game
import com.squareup.picasso.Picasso

class GameDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

        val game = intent.getSerializableExtra("GAME_DATA") as? Game
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener { finish() }

        if (game != null) {
            findViewById<TextView>(R.id.tvDetailTitle).text = game.title
            findViewById<TextView>(R.id.tvDetailGenre).text = "${game.genre} | ${game.platform}"
            // Usamos la descripción larga si existe, sino la corta
            findViewById<TextView>(R.id.tvDetailDescription).text = game.description ?: game.short_description
            findViewById<TextView>(R.id.tvDetailDeveloper).text = game.developer ?: "Desconocido"
            findViewById<TextView>(R.id.tvDetailDate).text = game.release_date ?: "N/A"

            val ivImage = findViewById<ImageView>(R.id.ivDetailImage)
            if (game.thumbnail.isNotEmpty()) {
                Picasso.get()
                    .load(game.thumbnail)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_delete)
                    .into(ivImage)
            }
        }
    }
}