package com.example.gamezone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamezone.api.ApiClient
import com.example.gamezone.models.LoginRequest
import com.example.gamezone.models.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

            // Conexión al Microservicio de Login
            val loginRequest = LoginRequest(email, password)
            ApiClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        val responseBody = response.body()!!
                        val user = responseBody.user
                        
                        if (user != null) {
                            // Guardar sesión en SharedPreferences
                            val prefs = getSharedPreferences("GameZonePrefs", Context.MODE_PRIVATE)
                            val editor = prefs.edit()
                            editor.putInt("USER_ID", user.id)
                            editor.putString("USER_NAME", user.fullName)
                            editor.putString("USER_EMAIL", user.email)
                            editor.putString("USER_PHONE", user.phone ?: "")
                            editor.putString("USER_GENRES", user.genres ?: "")
                            editor.apply()

                            Toast.makeText(this@LoginActivity, "Bienvenido, ${user.fullName}", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                             Toast.makeText(this@LoginActivity, "Error: Datos de usuario incompletos", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}