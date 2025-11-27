package com.example.gamezone.api

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import org.json.JSONObject

class MockInterceptor : Interceptor {
    
    // Almacenamiento temporal en memoria estática (simulando una BD del servidor)
    companion object {
        private var savedUser = JSONObject().apply {
            put("id", 1)
            put("fullName", "Usuario Demo")
            put("email", "mock@duoc.cl")
            put("phone", "99999999")
            put("genres", "Acción,Aventura")
            put("password", "Password123!")
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val uri = request.url().uri().toString()
        val method = request.method()
        
        var responseString = ""
        var responseCode = 200

        if (uri.endsWith("register") && method == "POST") {
            // Leer el cuerpo de la petición para guardar los datos reales
            val requestBodyStr = bodyToString(request)
            val jsonRequest = JSONObject(requestBodyStr)
            
            // Actualizamos nuestro "usuario guardado" con los datos reales del registro
            savedUser.put("fullName", jsonRequest.optString("fullName"))
            savedUser.put("email", jsonRequest.optString("email"))
            savedUser.put("password", jsonRequest.optString("password"))
            savedUser.put("phone", jsonRequest.optString("phone"))
            savedUser.put("genres", jsonRequest.optString("genres"))
            
            responseString = """
                {
                    "message": "Usuario registrado exitosamente",
                    "userId": 1
                }
            """
        } 
        else if (uri.endsWith("login") && method == "POST") {
            // En Login, verificamos credenciales (opcional) y devolvemos el usuario guardado
            val requestBodyStr = bodyToString(request)
            val jsonRequest = JSONObject(requestBodyStr)
            val email = jsonRequest.optString("email")
            
            // Para efectos de la demo, permitimos login si el email coincide o si es genérico,
            // pero devolvemos los datos que tenemos en 'savedUser'
            
            responseString = """
                {
                    "message": "Login exitoso",
                    "user": $savedUser
                }
            """
        } 
        else if (uri.contains("/users/") && method == "PUT") {
            // Al editar perfil, actualizamos 'savedUser' también
            val requestBodyStr = bodyToString(request)
            val jsonRequest = JSONObject(requestBodyStr)
            
            savedUser.put("fullName", jsonRequest.optString("fullName"))
            savedUser.put("email", jsonRequest.optString("email"))
            savedUser.put("phone", jsonRequest.optString("phone"))
            savedUser.put("genres", jsonRequest.optString("genres"))

            responseString = """
                {
                    "message": "Usuario actualizado correctamente",
                    "user": $savedUser
                }
            """
        } 
        else if (uri.contains("/users/") && method == "DELETE") {
             // Resetear usuario a valores por defecto
             savedUser = JSONObject().apply {
                put("id", 1)
                put("fullName", "Usuario Eliminado")
                put("email", "")
            }
            
            responseString = """
                {
                    "message": "Usuario eliminado correctamente"
                }
            """
        }

        if (responseString.isNotEmpty()) {
            // Simulamos un pequeño retraso de red para realismo
            Thread.sleep(300)
            
            return Response.Builder()
                .code(responseCode)
                .message(responseString)
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