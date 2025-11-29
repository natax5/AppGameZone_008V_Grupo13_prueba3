package com.example.gamezone

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.gamezone.db.AppDatabase
import com.example.gamezone.db.User
import com.example.gamezone.utils.Validators
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etRegisterName)
        val etEmail = findViewById<EditText>(R.id.etRegisterEmail)
        val etPassword = findViewById<EditText>(R.id.etRegisterPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etRegisterConfirmPassword)
        val etPhone = findViewById<EditText>(R.id.etRegisterPhone)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnBack = findViewById<Button>(R.id.btnBack)
        
        val checkBoxes = listOf<CheckBox>(
            findViewById(R.id.cbFiction),
            findViewById(R.id.cbNonFiction),
            findViewById(R.id.cbMystery),
            findViewById(R.id.cbHorror),
            findViewById(R.id.cbSuspense),
            findViewById(R.id.cbHistory)
        )

        btnBack.setOnClickListener {
            finish()
        }

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            
            // Validaciones
            if (!Validators.isValidName(name)) {
                etName.error = "Nombre inválido (solo letras, máx 100)"
                return@setOnClickListener
            }

            if (!Validators.isValidEmail(email)) {
                etEmail.error = "Debe ser un correo válido @duoc.cl o @admin.cl"
                return@setOnClickListener
            }

            if (!Validators.isValidPassword(password)) {
                etPassword.error = "Mínimo 10 chars: 1 Mayus, 1 Minus, 1 Num, 1 Especial"
                return@setOnClickListener
            }

            if (!Validators.doPasswordsMatch(password, confirmPassword)) {
                etConfirmPassword.error = "Las contraseñas no coinciden"
                return@setOnClickListener
            }
            
            val selectedGenres = checkBoxes.filter { it.isChecked }.map { it.text.toString() }
            if (selectedGenres.isEmpty()) {
                Toast.makeText(this, "Selecciona al menos un género", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Registro en Base de Datos Local (Room)
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(applicationContext)
                val existingUser = db.userDao().getUserByEmail(email)

                if (existingUser != null) {
                    Toast.makeText(this@RegisterActivity, "El correo ya está registrado", Toast.LENGTH_SHORT).show()
                } else {
                    val newUser = User(
                        fullName = name,
                        email = email,
                        password = password,
                        phone = if (phone.isEmpty()) null else phone,
                        genres = selectedGenres.joinToString(",")
                    )
                    db.userDao().insertUser(newUser)
                    Toast.makeText(this@RegisterActivity, "Usuario registrado con éxito", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
}