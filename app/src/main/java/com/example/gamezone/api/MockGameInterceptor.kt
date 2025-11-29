package com.example.gamezone.api

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import kotlin.random.Random

class MockGameInterceptor : Interceptor {
    
    companion object {
        // Juegos almacenados en memoria estática
        val gamesList: JSONArray

        init {
            val initialData = """
            [
                  {
                    "id": 1,
                    "title": "God of War: Ragnarok",
                    "short_description": "Kratos y Atreus viajan por los Nueve Reinos.",
                    "description": "Embárcate en un viaje épico y conmovedor mientras Kratos y Atreus luchan por aferrarse y soltarse.",
                    "thumbnail": "https://image.api.playstation.com/vulcan/ap/rnd/202207/1210/4xJ8XB3bi888QTLZYdl7Oi0s.png",
                    "genre": "Fantasy",
                    "platform": "PlayStation / PC",
                    "developer": "Santa Monica Studio",
                    "release_date": "2022-11-09"
                  },
                  {
                    "id": 2,
                    "title": "Cyberpunk 2077",
                    "short_description": "RPG de acción y aventura de mundo abierto en Night City.",
                    "description": "Cyberpunk 2077 es una historia de acción y aventura de mundo abierto ambientada en Night City.",
                    "thumbnail": "https://www.freetogame.com/g/466/thumbnail.jpg",
                    "genre": "Sci-Fi",
                    "platform": "PC (Windows)",
                    "developer": "CD Projekt Red",
                    "release_date": "2020-12-10"
                  },
                  {
                    "id": 3,
                    "title": "Starfield",
                    "short_description": "RPG de nueva generación ambientado entre las estrellas.",
                    "description": "Starfield es el primer universo nuevo en 25 años de Bethesda Game Studios.",
                    "thumbnail": "https://cdn.cloudflare.steamstatic.com/steam/apps/1716740/header.jpg",
                    "genre": "Sci-Fi",
                    "platform": "PC / Xbox",
                    "developer": "Bethesda Game Studios",
                    "release_date": "2023-09-06"
                  },
                  {
                    "id": 4,
                    "title": "FIFA 23",
                    "short_description": "El Juego del Mundo.",
                    "description": "Experimenta el Juego del Mundo con más de 19,000 jugadores, más de 700 equipos.",
                    "thumbnail": "https://www.freetogame.com/g/554/thumbnail.jpg",
                    "genre": "Sports",
                    "platform": "PC / Console",
                    "developer": "EA Sports",
                    "release_date": "2022-09-30"
                  },
                  {
                    "id": 5,
                    "title": "NBA 2K24",
                    "short_description": "Experimenta la cultura del baloncesto en NBA 2K24.",
                    "description": "Reúne a tu equipo y experimenta el pasado, presente y futuro de la cultura del baloncesto en NBA 2K24.",
                    "thumbnail": "https://cdn.cloudflare.steamstatic.com/steam/apps/2338770/header.jpg",
                    "genre": "Sports",
                    "platform": "PC / Console",
                    "developer": "Visual Concepts",
                    "release_date": "2023-09-08"
                  },
                  {
                    "id": 6,
                    "title": "Forza Horizon 5",
                    "short_description": "La aventura de conducción definitiva.",
                    "description": "Explora los paisajes vibrantes y en constante evolución del mundo abierto de México.",
                    "thumbnail": "https://www.freetogame.com/g/557/thumbnail.jpg",
                    "genre": "Racing",
                    "platform": "PC / Xbox",
                    "developer": "Playground Games",
                    "release_date": "2021-11-09"
                  },
                  {
                    "id": 7,
                    "title": "Resident Evil 4 Remake",
                    "short_description": "Sobrevivir es solo el comienzo.",
                    "description": "Han pasado seis años desde el desastre biológico en Raccoon City.",
                    "thumbnail": "https://cdn.cloudflare.steamstatic.com/steam/apps/2050650/header.jpg",
                    "genre": "Horror",
                    "platform": "PC / Console",
                    "developer": "Capcom",
                    "release_date": "2023-03-24"
                  },
                  {
                    "id": 8,
                    "title": "Dead by Daylight",
                    "short_description": "La muerte no es una escapatoria.",
                    "description": "Dead by Daylight es un juego de terror multijugador (4 contra 1).",
                    "thumbnail": "https://cdn.cloudflare.steamstatic.com/steam/apps/381210/header.jpg",
                    "genre": "Horror",
                    "platform": "PC / Console",
                    "developer": "Behaviour Interactive",
                    "release_date": "2016-06-14"
                  },
                  {
                    "id": 9,
                    "title": "Left 4 Dead 2",
                    "short_description": "Apocalipsis Zombie Cooperativo.",
                    "description": "Ambientado en el apocalipsis zombie, Left 4 Dead 2 (L4D2) es la secuela del galardonado Left 4 Dead.",
                    "thumbnail": "https://cdn.cloudflare.steamstatic.com/steam/apps/550/header.jpg",
                    "genre": "Zombie",
                    "platform": "PC",
                    "developer": "Valve",
                    "release_date": "2009-11-17"
                  },
                  {
                    "id": 10,
                    "title": "Assassin's Creed Mirage",
                    "short_description": "Vive la historia de Basim.",
                    "description": "En Assassin’s Creed Mirage, eres Basim, un astuto ladrón callejero con visiones de pesadilla.",
                    "thumbnail": "https://cdn.cloudflare.steamstatic.com/steam/apps/2355460/header.jpg",
                    "genre": "Historical",
                    "platform": "PC / Console",
                    "developer": "Ubisoft",
                    "release_date": "2023-10-05"
                  },
                  {
                    "id": 11,
                    "title": "Age of Empires IV",
                    "short_description": "Regreso a la historia.",
                    "description": "Uno de los juegos de estrategia en tiempo real más queridos regresa a la gloria.",
                    "thumbnail": "https://cdn.cloudflare.steamstatic.com/steam/apps/1466860/header.jpg",
                    "genre": "Strategy",
                    "platform": "PC",
                    "developer": "Relic Entertainment",
                    "release_date": "2021-10-28"
                  },
                   {
                    "id": 12,
                    "title": "Civilization VI",
                    "short_description": "Construye un imperio que resista el paso del tiempo.",
                    "description": "Civilization VI ofrece nuevas formas de interactuar con tu mundo.",
                    "thumbnail": "https://cdn.cloudflare.steamstatic.com/steam/apps/289070/header.jpg",
                    "genre": "Strategy",
                    "platform": "PC",
                    "developer": "Firaxis Games",
                    "release_date": "2016-10-21"
                  },
                  {
                    "id": 13,
                    "title": "Among Us",
                    "short_description": "Un juego online de trabajo en equipo y traición.",
                    "description": "Juega online o por WiFi local con 4-15 jugadores mientras intentas preparar tu nave espacial.",
                    "thumbnail": "https://cdn.cloudflare.steamstatic.com/steam/apps/945360/header.jpg",
                    "genre": "Mystery",
                    "platform": "PC / Mobile",
                    "developer": "Innersloth",
                    "release_date": "2018-11-16"
                  },
                  {
                    "id": 14,
                    "title": "Phasmophobia",
                    "short_description": "Terror psicológico cooperativo.",
                    "description": "Phasmophobia es un terror psicológico cooperativo online para 4 jugadores.",
                    "thumbnail": "https://cdn.cloudflare.steamstatic.com/steam/apps/739630/header.jpg",
                    "genre": "Mystery",
                    "platform": "PC",
                    "developer": "Kinetic Games",
                    "release_date": "2020-09-18"
                  },
                  {
                    "id": 15,
                    "title": "Hearthstone",
                    "short_description": "Juego de Cartas de Estrategia.",
                    "description": "Engañosamente simple e increíblemente divertido. ¡Recoge tus cartas y lanza el guante!",
                    "thumbnail": "https://www.freetogame.com/g/23/thumbnail.jpg",
                    "genre": "Card Game",
                    "platform": "PC / Mobile",
                    "developer": "Blizzard",
                    "release_date": "2014-03-11"
                  },
                  {
                    "id": 16,
                    "title": "Microsoft Flight Simulator",
                    "short_description": "El cielo te llama.",
                    "description": "Desde aviones ligeros hasta jets de fuselaje ancho, pilota aviones muy detallados.",
                    "thumbnail": "https://cdn.cloudflare.steamstatic.com/steam/apps/1250410/header.jpg",
                    "genre": "Simulation",
                    "platform": "PC / Xbox",
                    "developer": "Asobo Studio",
                    "release_date": "2020-08-18"
                  }
            ]
            """
            
            // Parseamos el JSON y añadimos precio y stock aleatorios a cada elemento
            val tempArray = JSONArray(initialData)
            gamesList = JSONArray()
            
            for (i in 0 until tempArray.length()) {
                val game = tempArray.getJSONObject(i)
                
                // Generar precio aleatorio entre 10.000 y 60.000 (múltiplos de 1000 para que se vea bonito)
                val price = (Random.nextInt(10, 61)) * 1000
                
                // Generar stock aleatorio entre 0 y 50
                val stock = Random.nextInt(0, 51)
                
                game.put("price", price)
                game.put("stock", stock)
                
                gamesList.put(game)
            }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val uri = request.url().uri().toString()
        val method = request.method()
        
        if (uri.contains("games")) {
            
            if (method == "POST") {
                // Si se agrega un juego simulado (legacy), le ponemos datos aleatorios
                val jsonResponse = """{ "message": "Juego agregado exitosamente" }"""
                return Response.Builder()
                    .code(201)
                    .message(jsonResponse)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(ResponseBody.create(MediaType.parse("application/json"), jsonResponse.toByteArray()))
                    .build()
            }
            
            val jsonResponse = gamesList.toString()
            
            return Response.Builder()
                .code(200)
                .message(jsonResponse)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(MediaType.parse("application/json"), jsonResponse.toByteArray()))
                .addHeader("content-type", "application/json")
                .build()
        }

        return chain.proceed(chain.request())
    }
}