package com.example.gamezone

import com.example.gamezone.utils.Validators
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationUnitTest {

    // Pruebas Unitarias para cubrir el 80% del código lógico de validación (IE 3.2.1)

    // --- Email Validation Tests ---
    @Test
    fun emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(Validators.isValidEmail("usuario@duoc.cl"))
    }

    @Test
    fun emailValidator_IncorrectDomain_ReturnsFalse() {
        assertFalse(Validators.isValidEmail("usuario@gmail.com"))
    }
    
    @Test
    fun emailValidator_EmptyEmail_ReturnsFalse() {
        assertFalse(Validators.isValidEmail(""))
    }

    @Test
    fun emailValidator_NoAtSymbol_ReturnsFalse() {
        assertFalse(Validators.isValidEmail("usuarioduoc.cl"))
    }

    // --- Password Validation Tests ---
    @Test
    fun passwordValidator_SecurePassword_ReturnsTrue() {
        assertTrue(Validators.isValidPassword("Password123!"))
    }

    @Test
    fun passwordValidator_NoUpperCase_ReturnsFalse() {
        assertFalse(Validators.isValidPassword("password123!"))
    }
    
    @Test
    fun passwordValidator_NoLowerCase_ReturnsFalse() {
        assertFalse(Validators.isValidPassword("PASSWORD123!"))
    }
    
    @Test
    fun passwordValidator_NoDigit_ReturnsFalse() {
        assertFalse(Validators.isValidPassword("Password!"))
    }

    @Test
    fun passwordValidator_NoSpecialChar_ReturnsFalse() {
        assertFalse(Validators.isValidPassword("Password123"))
    }

    @Test
    fun passwordValidator_TooShort_ReturnsFalse() {
        assertFalse(Validators.isValidPassword("Pass1!"))
    }

    // --- Name Validation Tests ---
    @Test
    fun nameValidator_ValidName_ReturnsTrue() {
        assertTrue(Validators.isValidName("Juan Perez"))
    }

    @Test
    fun nameValidator_NumbersInName_ReturnsFalse() {
        assertFalse(Validators.isValidName("Juan Perez 123"))
    }
    
    @Test
    fun nameValidator_EmptyName_ReturnsFalse() {
        assertFalse(Validators.isValidName(""))
    }

    // --- Phone Validation Tests ---
    @Test
    fun phoneValidator_ValidPhone_ReturnsTrue() {
        assertTrue(Validators.isValidPhone("912345678"))
    }
    
    @Test
    fun phoneValidator_EmptyPhone_ReturnsTrue() {
        // Es opcional según la lógica
        assertTrue(Validators.isValidPhone("")) 
    }
    
    @Test
    fun phoneValidator_InvalidCharacters_ReturnsFalse() {
        assertFalse(Validators.isValidPhone("abcde"))
    }
}