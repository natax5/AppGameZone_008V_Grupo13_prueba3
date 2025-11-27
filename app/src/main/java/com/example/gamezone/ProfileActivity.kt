package com.example.gamezone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gamezone.api.ApiClient
import com.example.gamezone.models.UpdateUserRequest
import com.example.gamezone.models.UpdateUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {

    private var userId: Int = -1
    private lateinit var checkBoxes: List<CheckBox>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val etName = findViewById<EditText>(R.id.etEditName)
        val etEmail = findViewById<EditText>(R.id.etEditEmail)
        val etPhone = findViewById<EditText>(R.id.etEditPhone)
        // El EditText 'etEditGenres' fue removido en el layout nuevo, ahora usamos CheckBox
        // val etGenres = findViewById<EditText>(R.id.etEditGenres) 
        
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val btnBack = findViewById<Button>(R.id.btnBack)

        checkBoxes = listOf(
            findViewById(R.id.cbEditFiction),
            findViewById(R.id.cbEditNonFiction),
            findViewById(R.id.cbEditMystery),
            findViewById(R.id.cbEditHorror),
            findViewById(R.id.cbEditSuspense),
            findViewById(R.id.cbEditHistory)
        )

        // Cargar datos guardados en SharedPreferences
        val prefs = getSharedPreferences("GameZonePrefs", Context.MODE_PRIVATE)
        userId = prefs.getInt("USER_ID", -1)
        etName.setText(prefs.getString("USER_NAME", ""))
        etEmail.setText(prefs.getString("USER_EMAIL", ""))
        etPhone.setText(prefs.getString("USER_PHONE", ""))
        
        // Cargar géneros en los CheckBox
        val savedGenres = prefs.getString("USER_GENRES", "") ?: ""
        val genresList = savedGenres.split(",").map { it.trim() }
        
        checkBoxes.forEach { cb ->
            if (genresList.contains(cb.text.toString())) {
                cb.isChecked = true
            }
        }

        if (userId == -1) {
            Toast.makeText(this, "Error de sesión", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        btnBack.setOnClickListener {
            finish()
        }

        btnUpdate.setOnClickListener {
            // Recopilar géneros seleccionados
            val selectedGenres = checkBoxes.filter { it.isChecked }.map { it.text.toString() }
            val genresString = selectedGenres.joinToString(",")

            val request = UpdateUserRequest(
                fullName = etName.text.toString(),
                email = etEmail.text.toString(),
                phone = etPhone.text.toString(),
                genres = genresString
            )

            ApiClient.instance.updateUser(userId, request).enqueue(object : Callback<UpdateUserResponse> {
                override fun onResponse(call: Call<UpdateUserResponse>, response: Response<UpdateUserResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProfileActivity, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                        
                        // Actualizar localmente para que MainActivity lo vea al volver
                        val editor = prefs.edit()
                        editor.putString("USER_NAME", request.fullName)
                        editor.putString("USER_EMAIL", request.email)
                        editor.putString("USER_PHONE", request.phone)
                        editor.putString("USER_GENRES", request.genres)
                        editor.apply()
                        
                    } else {
                        Toast.makeText(this@ProfileActivity, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UpdateUserResponse>, t: Throwable) {
                    Toast.makeText(this@ProfileActivity, "Error de red", Toast.LENGTH_SHORT).show()
                }
            })
        }

        btnDelete.setOnClickListener {
            ApiClient.instance.deleteUser(userId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ProfileActivity, "Cuenta eliminada", Toast.LENGTH_SHORT).show()
                        
                        val editor = prefs.edit()
                        editor.clear()
                        editor.apply()
                        
                        val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@ProfileActivity, "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@ProfileActivity, "Error de red", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}