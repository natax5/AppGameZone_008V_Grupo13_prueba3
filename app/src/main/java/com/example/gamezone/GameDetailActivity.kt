package com.example.gamezone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gamezone.db.AppDatabase
import com.example.gamezone.db.CartItem
import com.example.gamezone.models.Game
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class GameDetailActivity : AppCompatActivity() {
    
    private var currentQuantity = 1
    private var maxStock = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

        val game = intent.getSerializableExtra("GAME_DATA") as? Game
        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnEdit = findViewById<Button>(R.id.btnEditGame)
        val btnAddToCart = findViewById<Button>(R.id.btnAddToCart)
        val btnDecrease = findViewById<Button>(R.id.btnDecreaseQty)
        val btnIncrease = findViewById<Button>(R.id.btnIncreaseQty)
        val tvQuantity = findViewById<TextView>(R.id.tvQuantity)

        btnBack.setOnClickListener { finish() }

        if (game != null) {
            maxStock = game.stock
            
            findViewById<TextView>(R.id.tvDetailTitle).text = game.title
            findViewById<TextView>(R.id.tvDetailGenre).text = "${game.genre} | ${game.platform}"
            findViewById<TextView>(R.id.tvDetailDescription).text = game.description ?: game.short_description
            findViewById<TextView>(R.id.tvDetailDeveloper).text = game.developer ?: "Desconocido"
            findViewById<TextView>(R.id.tvDetailDate).text = game.release_date ?: "N/A"
            
            findViewById<TextView>(R.id.tvDetailPrice).text = "Precio: $${game.price}"
            findViewById<TextView>(R.id.tvDetailStock).text = "Stock: ${game.stock}"

            val ivImage = findViewById<ImageView>(R.id.ivDetailImage)
            if (game.thumbnail.isNotEmpty()) {
                Picasso.get()
                    .load(game.thumbnail)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_delete)
                    .into(ivImage)
            }
            
            // Lógica de Selector de Cantidad
            btnDecrease.setOnClickListener {
                if (currentQuantity > 1) {
                    currentQuantity--
                    tvQuantity.text = currentQuantity.toString()
                }
            }

            btnIncrease.setOnClickListener {
                if (currentQuantity < maxStock) {
                    currentQuantity++
                    tvQuantity.text = currentQuantity.toString()
                } else {
                    Toast.makeText(this, "Stock máximo alcanzado", Toast.LENGTH_SHORT).show()
                }
            }
            
            val prefs = getSharedPreferences("GameZonePrefs", Context.MODE_PRIVATE)
            val isAdmin = prefs.getBoolean("IS_ADMIN", false)
            val userId = prefs.getInt("USER_ID", -1)
            
            // Lógica de Agregar al Carrito
            btnAddToCart.setOnClickListener {
                if (game.stock <= 0) {
                    Toast.makeText(this, "Producto agotado", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (userId == -1) {
                    Toast.makeText(this, "Debes iniciar sesión para comprar", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(applicationContext)
                    val existingItem = db.cartDao().getItemByGameId(userId, game.id)
                    
                    if (existingItem != null) {
                        // Verificar si al sumar supera el stock
                        val newTotal = existingItem.quantity + currentQuantity
                        if (newTotal <= maxStock) {
                            db.cartDao().updateQuantity(existingItem.id, newTotal)
                            Toast.makeText(this@GameDetailActivity, "Se agregaron $currentQuantity unidades más", Toast.LENGTH_SHORT).show()
                        } else {
                             Toast.makeText(this@GameDetailActivity, "No hay suficiente stock para agregar más", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Nuevo item
                        val cartItem = CartItem(
                            userId = userId,
                            gameId = game.id,
                            title = game.title,
                            price = game.price,
                            quantity = currentQuantity,
                            thumbnail = game.thumbnail,
                            isLocalGame = game.isLocal
                        )
                        db.cartDao().insertItem(cartItem)
                        Toast.makeText(this@GameDetailActivity, "Agregado al carrito ($currentQuantity)", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Lógica de botón Editar
            if (isAdmin && game.isLocal) {
                btnEdit.visibility = View.VISIBLE
                btnEdit.setOnClickListener {
                    val intent = Intent(this, AddGameActivity::class.java)
                    val realId = game.id - 10000
                    intent.putExtra("EDIT_GAME_ID", realId)
                    startActivity(intent)
                    finish()
                }
            } else {
                btnEdit.visibility = View.GONE
            }
        }
    }
}