package com.example.gamezone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gamezone.db.AppDatabase
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Campos vacíos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Login con Base de Datos Local (Room)
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                val user = db.userDao().login(email, password)

                if (user != null) {
                    // Determinar rol
                    val isAdmin = user.email.endsWith("@admin.cl")
                    val roleName = if (isAdmin) "Administrador" else "Cliente"

                    // Guardar sesión en SharedPreferences
                    val prefs = getSharedPreferences("GameZonePrefs", Context.MODE_PRIVATE)
                    val editor = prefs.edit()
                    editor.putInt("USER_ID", user.id)
                    editor.putString("USER_NAME", user.fullName)
                    editor.putString("USER_EMAIL", user.email)
                    editor.putString("USER_PHONE", user.phone ?: "")
                    editor.putString("USER_GENRES", user.genres)
                    editor.putBoolean("IS_ADMIN", isAdmin)
                    editor.apply()

                    Toast.makeText(this@LoginActivity, "Bienvenido $roleName, ${user.fullName}", Toast.LENGTH_SHORT).show()
                    
                    // Si es admin, podríamos redirigirlo a otra pantalla, pero por ahora va al Main
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}