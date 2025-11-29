package com.example.gamezone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gamezone.db.AppDatabase
import com.example.gamezone.db.User
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private var userId: Int = -1
    private lateinit var checkBoxes: List<CheckBox>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val etName = findViewById<EditText>(R.id.etEditName)
        val etEmail = findViewById<EditText>(R.id.etEditEmail)
        val etPhone = findViewById<EditText>(R.id.etEditPhone)
        
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

            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                
                // Necesitamos la contraseña actual para actualizar el objeto User
                // (En una app real pediríamos confirmación o la leeríamos de la BD)
                val currentUser = db.userDao().getUserByEmail(prefs.getString("USER_EMAIL", "") ?: "")
                
                if (currentUser != null) {
                    val updatedUser = User(
                        id = userId,
                        fullName = etName.text.toString(),
                        email = etEmail.text.toString(),
                        password = currentUser.password, // Mantenemos la contraseña existente
                        phone = etPhone.text.toString(),
                        genres = genresString
                    )

                    db.userDao().updateUser(updatedUser)

                    // Actualizar SharedPreferences
                    val editor = prefs.edit()
                    editor.putString("USER_NAME", updatedUser.fullName)
                    editor.putString("USER_EMAIL", updatedUser.email)
                    editor.putString("USER_PHONE", updatedUser.phone)
                    editor.putString("USER_GENRES", updatedUser.genres)
                    editor.apply()
                    
                    Toast.makeText(this@ProfileActivity, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProfileActivity, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnDelete.setOnClickListener {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                db.userDao().deleteUser(userId)

                Toast.makeText(this@ProfileActivity, "Cuenta eliminada", Toast.LENGTH_SHORT).show()
                
                val editor = prefs.edit()
                editor.clear()
                editor.apply()
                
                val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}