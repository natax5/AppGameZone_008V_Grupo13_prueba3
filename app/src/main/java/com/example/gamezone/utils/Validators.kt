package com.example.gamezone.utils

import android.util.Patterns
import java.util.regex.Pattern

object Validators {

    // Se permite @duoc.cl (Clientes) y @admin.cl (Administradores)
    fun isValidEmail(email: String): Boolean {
        return if (email.contains("@")) {
            val isValidFormat = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            val isAllowedDomain = email.endsWith("@duoc.cl") || email.endsWith("@admin.cl")
            val isValidLength = email.length <= 60
            
            isValidFormat && isAllowedDomain && isValidLength
        } else {
            false
        }
    }

    fun isValidName(name: String): Boolean {
        return name.isNotEmpty() && name.matches(Regex("^[a-zA-Z ]+\$")) && name.length <= 100
    }

    fun isValidPassword(password: String): Boolean {
        val passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{10,}\$")
        return passwordPattern.matcher(password).matches()
    }
    
    fun doPasswordsMatch(pass1: String, pass2: String): Boolean {
        return pass1 == pass2
    }

    fun isValidPhone(phone: String?): Boolean {
        // El teléfono es opcional, pero si se ingresa debe ser válido (ej: solo números)
        if (phone.isNullOrEmpty()) return true
        return Patterns.PHONE.matcher(phone).matches()
    }
}