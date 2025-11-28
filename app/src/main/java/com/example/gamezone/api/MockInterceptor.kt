package com.example.gamezone.api

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import org.json.JSONArray
import org.json.JSONObject

class MockInterceptor : Interceptor {
    
    // Simulación de base de datos de usuarios en memoria
    companion object {
        val usersList = JSONArray().apply {
            // Usuario Admin por defecto
            put(JSONObject().apply {
                put("id", 1)
                put("fullName", "Administrador Principal")
                put("email", "admin@admin.cl")
                put("password", "Admin123!")
                put("phone", "123456789")
                put("genres", "Todos")
            })
            // Usuario Cliente por defecto
            put(JSONObject().apply {
                put("id", 2)
                put("fullName", "Cliente Demo")
                put("email", "usuario@duoc.cl")
                put("password", "User123!")
                put("phone", "987654321")
                put("genres", "Ficción,Terror")
            })
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val uri = request.url().uri().toString()
        val method = request.method()
        
        var responseString = ""
        var responseCode = 200

        // --- LOGIN ---
        if (uri.endsWith("login") && method == "POST") {
            val requestBodyStr = bodyToString(request)
            val jsonRequest = JSONObject(requestBodyStr)
            val email = jsonRequest.optString("email")
            val password = jsonRequest.optString("password")
            
            // Buscar usuario en la lista
            var foundUser: JSONObject? = null
            for (i in 0 until usersList.length()) {
                val user = usersList.getJSONObject(i)
                if (user.getString("email") == email) {
                    // En una app real validaríamos password, aquí lo permitimos para facilitar
                    foundUser = user
                    break
                }
            }

            if (foundUser != null) {
                responseString = """
                    {
                        "message": "Login exitoso",
                        "user": $foundUser
                    }
                """
            } else {
                responseCode = 401
                responseString = """{ "message": "Credenciales inválidas" }"""
            }
        } 
        // --- REGISTER ---
        else if (uri.endsWith("register") && method == "POST") {
            val requestBodyStr = bodyToString(request)
            val jsonRequest = JSONObject(requestBodyStr)
            
            // Crear nuevo usuario
            val newUser = JSONObject().apply {
                put("id", usersList.length() + 1)
                put("fullName", jsonRequest.optString("fullName"))
                put("email", jsonRequest.optString("email"))
                put("password", jsonRequest.optString("password"))
                put("phone", jsonRequest.optString("phone"))
                put("genres", jsonRequest.optString("genres"))
            }
            usersList.put(newUser)
            
            responseString = """
                {
                    "message": "Usuario registrado exitosamente",
                    "userId": ${newUser.getInt("id")}
                }
            """
            responseCode = 201
        } 
        // --- GET ALL USERS (Para Admin) ---
        else if (uri.endsWith("users") && method == "GET") {
            responseString = usersList.toString()
        }
        // --- UPDATE USER ---
        else if (uri.contains("/users/") && method == "PUT") {
            // Extraer ID de la URL
            val idStr = uri.substringAfterLast("/")
            val id = idStr.toIntOrNull() ?: -1
            
            val requestBodyStr = bodyToString(request)
            val jsonRequest = JSONObject(requestBodyStr)
            
            var updatedUser: JSONObject? = null
            
            for (i in 0 until usersList.length()) {
                val user = usersList.getJSONObject(i)
                if (user.getInt("id") == id) {
                    user.put("fullName", jsonRequest.optString("fullName"))
                    user.put("email", jsonRequest.optString("email"))
                    user.put("phone", jsonRequest.optString("phone"))
                    user.put("genres", jsonRequest.optString("genres"))
                    updatedUser = user
                    break
                }
            }

            if (updatedUser != null) {
                responseString = """
                    {
                        "message": "Usuario actualizado correctamente",
                        "user": $updatedUser
                    }
                """
            } else {
                responseCode = 404
                responseString = """{ "message": "Usuario no encontrado" }"""
            }
        } 
        // --- DELETE USER ---
        else if (uri.contains("/users/") && method == "DELETE") {
            val idStr = uri.substringAfterLast("/")
            val id = idStr.toIntOrNull() ?: -1
            
            var removedIndex = -1
            for (i in 0 until usersList.length()) {
                if (usersList.getJSONObject(i).getInt("id") == id) {
                    removedIndex = i
                    break
                }
            }
            
            if (removedIndex != -1) {
                usersList.remove(removedIndex)
                responseString = """{ "message": "Usuario eliminado correctamente" }"""
            } else {
                responseCode = 404
                responseString = """{ "message": "Usuario no encontrado" }"""
            }
        }

        if (responseString.isNotEmpty()) {
            Thread.sleep(300)
            return Response.Builder()
                .code(responseCode)
                .message(if (responseCode == 200) "OK" else "Error")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(MediaType.parse("application/json"), responseString.toByteArray()))
                .addHeader("content-type", "application/json")
                .build()
        }

        return chain.proceed(chain.request())
    }

    private fun bodyToString(request: okhttp3.Request): String {
        return try {
            val copy = request.newBuilder().build()
            val buffer = Buffer()
            copy.body()?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: Exception) {
            "{}"
        }
    }
}