package com.example.gamezone

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamezone.api.ApiClient
import com.example.gamezone.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.ResponseBody

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val btnAddGame = findViewById<Button>(R.id.btnAddGame)
        val btnManageUsers = findViewById<Button>(R.id.btnManageUsers)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnAddGame.setOnClickListener {
            // Simular agregar juego
            // En una app real abriría un formulario, aquí llamamos al endpoint POST mockeado
            RetrofitClient.instance.addGame(HashMap()).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdminActivity, "Juego de prueba agregado exitosamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@AdminActivity, "Error al agregar juego", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@AdminActivity, "Error de red", Toast.LENGTH_SHORT).show()
                }
            })
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