package com.example.gamezone.api

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject

class MockGameInterceptor : Interceptor {
    
    companion object {
        // Juegos almacenados en memoria estática para que los cambios del admin persistan durante la sesión
        // Inicializamos con los juegos por defecto
        val gamesList = JSONArray("""
            [
                  {
                    "id": 1,
                    "title": "God of War: Ragnarok",
                    "short_description": "Kratos y Atreus viajan por los Nueve Reinos.",
                    "description": "Embárcate en un viaje épico y conmovedor mientras Kratos y Atreus luchan por aferrarse y soltarse. Con los Reinos Nórdicos desgarrados por la furia de los Aesir como telón de fondo, han estado haciendo todo lo posible para deshacer el fin de los tiempos... pero a pesar de sus mejores esfuerzos, Fimbulwinter avanza.",
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
                    "description": "Cyberpunk 2077 es una historia de acción y aventura de mundo abierto ambientada en Night City, una megalópolis obsesionada con el poder, el glamour y la modificación corporal. Juegas como V, un mercenario forajido que busca un implante único que es la clave de la inmortalidad.",
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
                    "description": "Starfield es el primer universo nuevo en 25 años de Bethesda Game Studios, los galardonados creadores de The Elder Scrolls V: Skyrim y Fallout 4. Crea el personaje que quieras y explora con una libertad inigualable.",
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
                    "description": "Experimenta el Juego del Mundo con más de 19,000 jugadores, más de 700 equipos, más de 100 estadios y más de 30 ligas. Juega como las mayores estrellas y equipos del fútbol mundial.",
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
                    "description": "Reúne a tu equipo y experimenta el pasado, presente y futuro de la cultura del baloncesto en NBA 2K24. Disfruta de montones de acción pura y sin adulterar y opciones ilimitadas de Mi JUGADOR personalizado.",
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
                    "description": "Explora los paisajes vibrantes y en constante evolución del mundo abierto de México con una acción de conducción ilimitada y divertida en cientos de los mejores coches del mundo.",
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
                    "description": "Han pasado seis años desde el desastre biológico en Raccoon City. El agente Leon S. Kennedy, uno de los supervivientes del incidente, ha sido enviado a rescatar a la hija secuestrada del presidente.",
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
                    "description": "Dead by Daylight es un juego de terror multijugador (4 contra 1) en el que un jugador asume el papel del salvaje Asesino, y los otros cuatro jugadores juegan como Supervivientes, intentando escapar del Asesino.",
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
                    "description": "Ambientado en el apocalipsis zombie, Left 4 Dead 2 (L4D2) is la muy esperada secuela del galardonado Left 4 Dead, el juego cooperativo n.º 1 de 2008.",
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
                    "description": "En Assassin’s Creed Mirage, eres Basim, un astuto ladrón callejero con visiones de pesadilla que busca respuestas y justicia. Únete a una antigua organización y comprende un nuevo credo.",
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
                    "description": "Uno de los juegos de estrategia en tiempo real más queridos regresa a la gloria con Age of Empires IV, colocándote en el centro de batallas históricas épicas que dieron forma al mundo.",
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
                    "description": "Civilization VI ofrece nuevas formas de interactuar con tu mundo, expandir tu imperio por el mapa, hacer avanzar tu cultura y competir contra los líderes más grandes de la historia.",
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
                    "description": "Juega online o por WiFi local con 4-15 jugadores mientras intentas preparar tu nave espacial para la partida, ¡pero cuidado, ya que uno será un impostor decidido a matar a todos!",
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
                    "description": "Phasmophobia es un terror psicológico cooperativo online para 4 jugadores. La actividad paranormal va en aumento y depende de ti y de tu equipo usar todo el equipo de caza de fantasmas a vuestra disposición.",
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
                    "description": "Engañosamente simple e increíblemente divertido. ¡Recoge tus cartas y lanza el guante! En Hearthstone, juegas al héroe en un juego de cartas de estrategia astuta, rápido y caprichoso.",
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
                    "description": "Desde aviones ligeros hasta jets de fuselaje ancho, pilota aviones muy detallados y precisos en la próxima generación de Microsoft Flight Simulator. Pon a prueba tus habilidades de pilotaje.",
                    "thumbnail": "https://cdn.cloudflare.steamstatic.com/steam/apps/1250410/header.jpg",
                    "genre": "Simulation",
                    "platform": "PC / Xbox",
                    "developer": "Asobo Studio",
                    "release_date": "2020-08-18"
                  }
            ]
        """)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val uri = request.url().uri().toString()
        val method = request.method()
        
        if (uri.contains("games")) {
            
            // Simulamos agregar un juego nuevo si es POST (solo para Admins)
            if (method == "POST") {
                 val jsonResponse = """
                    { "message": "Juego agregado exitosamente" }
                """
                
                // No podemos leer el body y parsearlo fácilmente aquí sin librerías externas complejas
                // Pero simularemos que se agregó uno genérico para efectos de la demo
                val newGame = JSONObject().apply {
                    put("id", gamesList.length() + 1)
                    put("title", "Nuevo Juego Agregado")
                    put("short_description", "Juego agregado por el administrador.")
                    put("description", "Este es un juego de prueba agregado desde el panel de administración.")
                    put("thumbnail", "https://via.placeholder.com/300")
                    put("genre", "Action")
                    put("platform", "PC")
                    put("developer", "Admin")
                    put("release_date", "2023-11-27")
                }
                gamesList.put(newGame)
                
                return Response.Builder()
                    .code(201)
                    .message(jsonResponse)
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(ResponseBody.create(MediaType.parse("application/json"), jsonResponse.toByteArray()))
                    .build()
            }
            
            // GET normal retorna la lista (que puede haber crecido)
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