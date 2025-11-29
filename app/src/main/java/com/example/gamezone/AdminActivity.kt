package com.example.gamezone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val btnAddGame = findViewById<Button>(R.id.btnAddGame)
        val btnManageUsers = findViewById<Button>(R.id.btnManageUsers)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnAddGame.setText("AGREGAR JUEGO (LOCAL)")
        
        btnAddGame.setOnClickListener {
            // Abrir formulario para agregar juego
            startActivity(Intent(this, AddGameActivity::class.java))
        }

        btnManageUsers.setOnClickListener {
            // Navegar a la pantalla de gestión de usuarios
            startActivity(Intent(this, UserManagementActivity::class.java))
        }

        btnBack.setOnClickListener {
            finish()
        }
    }
}