package com.example.gamezone

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamezone.db.AppDatabase
import com.example.gamezone.db.CartItem
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class CartActivity : AppCompatActivity() {
    
    private lateinit var rvCartItems: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: Button
    private var cartItems = mutableListOf<CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        rvCartItems = findViewById(R.id.rvCartItems)
        tvTotal = findViewById(R.id.tvCartTotal)
        btnCheckout = findViewById(R.id.btnCheckout)
        val btnBack = findViewById<Button>(R.id.btnBack)

        rvCartItems.layoutManager = LinearLayoutManager(this)

        btnBack.setOnClickListener { finish() }

        loadCart()
        
        btnCheckout.setOnClickListener {
            processPayment()
        }
    }

    private fun loadCart() {
        val prefs = getSharedPreferences("GameZonePrefs", Context.MODE_PRIVATE)
        val userId = prefs.getInt("USER_ID", -1)

        if (userId == -1) {
            finish()
            return
        }

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            cartItems = db.cartDao().getCartItems(userId).toMutableList()
            
            rvCartItems.adapter = CartAdapter(cartItems)
            calculateTotal()
        }
    }

    private fun calculateTotal() {
        var total = 0
        for (item in cartItems) {
            total += (item.price * item.quantity)
        }
        tvTotal.text = "$$total"
    }
    
    private fun processPayment() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            return
        }
        
        val prefs = getSharedPreferences("GameZonePrefs", Context.MODE_PRIVATE)
        val userId = prefs.getInt("USER_ID", -1)
        
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            
            // Descontar stock de juegos locales
            for (item in cartItems) {
                if (item.isLocalGame) {
                    val localId = item.gameId - 10000
                    val localGame = db.gameDao().getGameById(localId)
                    
                    if (localGame != null) {
                        val newStock = localGame.stock - item.quantity
                        val finalStock = if (newStock < 0) 0 else newStock
                        val updatedGame = localGame.copy(stock = finalStock)
                        db.gameDao().updateGame(updatedGame)
                    }
                }
            }
            
            // Vaciar carrito
            db.cartDao().clearCart(userId)
            
            Toast.makeText(this@CartActivity, "¡Compra Exitosa!", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    inner class CartAdapter(private val items: MutableList<CartItem>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
        
        inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val title: TextView = view.findViewById(R.id.tvCartItemTitle)
            val price: TextView = view.findViewById(R.id.tvCartItemPrice)
            val quantity: TextView = view.findViewById(R.id.tvCartItemQuantity)
            val image: ImageView = view.findViewById(R.id.ivCartItemImage)
            val btnDelete: ImageButton = view.findViewById(R.id.btnRemoveItem)
            val btnIncrease: Button = view.findViewById(R.id.btnCartIncrease)
            val btnDecrease: Button = view.findViewById(R.id.btnCartDecrease)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
            return CartViewHolder(view)
        }

        override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
            val item = items[position]
            holder.title.text = item.title
            holder.price.text = "$${item.price}"
            holder.quantity.text = item.quantity.toString()
            
            if (item.thumbnail.isNotEmpty()) {
                Picasso.get().load(item.thumbnail).placeholder(R.mipmap.ic_launcher).into(holder.image)
            }

            // Botón Disminuir Cantidad
            holder.btnDecrease.setOnClickListener {
                if (item.quantity > 1) {
                    lifecycleScope.launch {
                        val db = AppDatabase.getDatabase(applicationContext)
                        val newQty = item.quantity - 1
                        db.cartDao().updateQuantity(item.id, newQty)
                        
                        // Actualizar objeto local y UI
                        item.copy(quantity = newQty).let { updatedItem ->
                            items[holder.adapterPosition] = updatedItem // Reemplazar en la lista
                            // Ojo: `items` contiene `CartItem` que es inmutable (data class).
                            // Para modificar la lista mutable necesitamos reemplazar el objeto.
                            // Como `item` es una copia local del bucle, mejor actualizar la lista directamente:
                            // Pero CartItem es val... tenemos que crear uno nuevo.
                        }
                        // Actualizamos la referencia en la lista
                        val updatedItem = item.copy(quantity = newQty)
                        items[holder.adapterPosition] = updatedItem
                        
                        notifyItemChanged(holder.adapterPosition)
                        calculateTotal()
                    }
                }
            }

            // Botón Aumentar Cantidad
            holder.btnIncrease.setOnClickListener {
                // Aquí deberíamos validar stock máximo de nuevo, pero para simplificar lo dejamos libre o con límite alto
                // Si quisieramos validar, tendríamos que consultar el juego original
                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(applicationContext)
                    
                    // Opcional: Validar stock real antes de aumentar
                    var canIncrease = true
                    if (item.isLocalGame) {
                        val localGame = db.gameDao().getGameById(item.gameId - 10000)
                        if (localGame != null && item.quantity >= localGame.stock) {
                            canIncrease = false
                            Toast.makeText(this@CartActivity, "Stock máximo alcanzado", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Para juegos mockeados, el stock se generó aleatoriamente en memoria y no lo tenemos aquí fácilmente 
                        // a menos que lo pasemos o consultemos de nuevo. Asumimos un límite arbitrario.
                        if (item.quantity >= 50) canIncrease = false
                    }

                    if (canIncrease) {
                        val newQty = item.quantity + 1
                        db.cartDao().updateQuantity(item.id, newQty)
                        
                        val updatedItem = item.copy(quantity = newQty)
                        items[holder.adapterPosition] = updatedItem
                        
                        notifyItemChanged(holder.adapterPosition)
                        calculateTotal()
                    }
                }
            }

            holder.btnDelete.setOnClickListener {
                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(applicationContext)
                    db.cartDao().deleteItem(item)
                    
                    items.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    notifyItemRangeChanged(holder.adapterPosition, items.size)
                    calculateTotal()
                    Toast.makeText(this@CartActivity, "Eliminado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        override fun getItemCount() = items.size
    }
}