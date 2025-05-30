package com.example.tesis.logica

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Categoria::class, Pregunta::class, Estudiante::class, Resultado::class,
    Recomendacion::class, Clasificacion::class, Respuesta::class], version = 4, exportSchema = false)

abstract class DbClass : RoomDatabase() {
    abstract fun categoriaDao(): CategoriaDao
    abstract fun preguntaDao(): PreguntaDao
    abstract fun estudianteDao(): EstudianteDao
    abstract fun resultadoDao(): ResultadoDao
    abstract fun recomendacionDao(): RecomendacionDao
    abstract fun clasificacionDao(): ClasificacionDao
    abstract fun respuestaDao(): RespuestaDao

    companion object {
        @Volatile private var INSTANCE: DbClass? = null

        fun getDatabase(context: Context): DbClass {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DbClass::class.java,
                    "soly_db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            // Insert into categorias
                            db.execSQL("INSERT INTO categorias (id, nombre) VALUES (1, 'Inteligencia Linguistica');")
                            db.execSQL("INSERT INTO categorias (id, nombre) VALUES (2, 'Inteligencia Lógico-Matemática');")
                            db.execSQL("INSERT INTO categorias (id, nombre) VALUES (3, 'Inteligencia Espacial');")
                            db.execSQL("INSERT INTO categorias (id, nombre) VALUES (4, 'Inteligencia Musical');")
                            db.execSQL("INSERT INTO categorias (id, nombre) VALUES (5, 'Inteligencia Corporal-Cinestésica');")
                            db.execSQL("INSERT INTO categorias (id, nombre) VALUES (6, 'Inteligencia Interpersonal');")
                            db.execSQL("INSERT INTO categorias (id, nombre) VALUES (7, 'Inteligencia Intrapersonal');")
                            db.execSQL("INSERT INTO categorias (id, nombre) VALUES (8, 'Inteligencia Naturalista');")

                            // Insert into clasificaciones
                            db.execSQL("INSERT INTO clasificaciones (id, nombre) VALUES (1, 'Posible deficiencia');")
                            db.execSQL("INSERT INTO clasificaciones (id, nombre) VALUES (2, 'Posible media');")
                            db.execSQL("INSERT INTO clasificaciones (id, nombre) VALUES (3, 'Posible proeficiencia');")

                            // Insert into preguntas
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (1, 'Soly quiere saber si tienes libros o revistas en casa', 'Si', 1, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (2, 'Cuentale a Soly si tienes instrumentos musicales en casa', 'Si', 4, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (3, 'Dile a Soly si tienes juguetes en casa', 'Si', 7, 1);")
                            //Preguntas Linguistica
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (4, 'Disfrutas leyendo cuentos o libros?', 'Si', 1, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (5, 'Cuando juegas con amigos o amigas, cuentas o inventas historias para divertirse?', 'Si', 1, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (6, 'Te gustan las clases de español?', 'Si', 1, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (7, 'Te gusta hablar con amigos y familia?', 'Si', 1, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (8, 'Leer te parece fácil o difícil?', 'Fácil', 1, 1);")
                            //Preguntas LogiMat
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (9, 'Puedes decirle a Soly cuántos botones tiene la camisa?', '8', 2, 3);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (10, 'Ayuda a Soly a contar las latas, cuántas latas abiertas y cuántas latas cerradas hay?', '1,3', 2, 3);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (11, 'Soly quiere contar, dile a Soly cuál es el siguiente número en la secuencia', '3,10,6,8,2,2', 2, 3);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (12, 'Soly quiere tu ayuda para contar las frutas, cuántas puedes ver de cada una?', '6,2,4,5,4', 2, 3);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (13, 'Soly llevó 5 peras para compartir con su amigo, si se comieron 3, cuántas quedaron?', '2', 2, 3);")
                            //Preguntas VisEsp
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (14, 'Soly necesita tu ayuda para volver a casa, dibuja el camino que debe seguir para llegar', '32-33-34-35-28-21-14-7-12-6-0-1-2-3', 3, 2);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (15, 'Ayuda a los amigos de Soly a llegar a su casilla correspondiente siguiendo el ejemplo en la imagen', 'A:1-2,B:1-1,C:0-3,D:3-0,E:3-2,F:2-3', 3, 4);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (16, 'Observa el mapa de Soly con mucha atención, puedes ayudarle a llegar a su tesoro?', '2,10,8', 3, 3);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (17, 'A Soly le encantan los insectos y quiere jugar contigo, ayudale a ubicarlos poniendo la cabeza hacia donde se orientan las flechas', 'D:0-0,B:0-1,C:0-2,A:0-3,E:1-0,G:1-1,H:1-2,F:1-3,L:2-0,K:2-1,I:2-2,J:2-3,M:3-0,O:3-1,P:3-3,N:3-2', 3, 4);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (18, 'Arma el rompecabezas con Soly', 'A:0-0,B:0-1,C:0-2,D:0-3,E:1-0,F:1-1,G:1-2,H:1-3,I:2-0,J:2-1,K:2-2,L:2-3,M:3-0,N:3-1,O:3-2,P:3-3', 3, 4);")
                            //Preguntas CorCin
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (19, 'Soly quiere conocerte mejor, dile a Soly si te gusta hacer deporte, saltar y correr', 'Si', 5, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (20, 'Dile a Soly si te gusta dormir', 'Si', 5, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (21, 'A Soly le gustaría saber si te gusta ver televisión', 'No', 5, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (22, 'Cuentale a Soly si te gusta hacer tareas y estudiar', 'No', 5, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (23, 'Utiliza al menos cinco de las siguientes figuras para construir algo', 'Si', 5, 5);")
                            //Preguntas Mus
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (24, 'Te gusta bailar?', 'Si', 4, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (25, 'Te interesa aprender a tocar algun instrumento?', 'Si', 4, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (26, 'Te gusta cantar?', 'Si', 4, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (27, 'Has hecho bailes o coreografias?', 'Si', 4, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (28, 'Cuando has bailado o realizado coreografias, lo haces con facilidad?', 'Si', 4, 1);")
                            //Preguntas Intra
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (29, 'Puedes ayudar a Soly a nombras las emociones según su color?', 'Feliz,Loco,Triste,Sorprendido,Enojado', 7, 3);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (30, 'Si dos amigos están disgustados, tú les ayudarías a resolver el problema?', 'Si', 7, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (31, 'Te gusta compartir con tus amigos y familia tus creaciones y dibujos?', 'Si', 7, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (32, 'Te gusta bañarte y elegir tu ropa?', 'Si', 7, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (33, 'Cuando intentas hacer algo nuevo y no puedes, vuelves a intentarlo?', 'Si', 7, 1);")
                            //Preguntas Inter
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (34, 'Si te dejan un trabajo en la escuela, que prefieres?', 'Grupo', 6, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (35, 'Si eres elegido como representante de tu clase, te gustaría serlo?', 'Si', 6, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (36, 'Si tu maestro te pide que ayudes como vendedor en la tienda del colegio, te gustaría hacerlo?', 'Si', 6, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (37, 'Te parece que los niños de la imagen necesitan ayuda?', 'Si', 6, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (38, 'Tu ayudarias a los niños que viste antes?', 'Si', 6, 1);")
                            //Preguntas Natu
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (39, 'Ayuda a Soly a clasificar los animales', 'A:Animales de granja,B:Dinosaurios,C:Dinosaurios,D:Dinosaurios,E:Animales de granja,F:Animales de granja,G:Animales de granja,H:Dinosaurios,I:Animales de granja,J:Dinosaurios', 8, 5);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (40, 'Soly quiere relacionar cada uno de los animales con su propia sombra, ayudale!', 'A:Gorila,B:Pez,C:Caracol,D:Zebra,E:Pajaro', 8, 5);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (41, 'Observa los objetos y ayuda a Soly a organizarlos en conjuntos de acuerdo a lo que tienen en comun', 'A:Utiles Escolares,B:Utiles Escolares,C:Animales,D:Frutas,E:Frutas,F:Animales,G:Animales,H:Utiles Escolares,I:Frutas', 8, 5);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (42, 'Te gusta observar los animales y las plantas?', 'Si', 8, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (43, 'Si tu maestro te invita a buscar con una lupa animales en el patio, te gustaría hacerlo?', 'Si', 8, 1);")

                            // Insert into respuestas
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (1, 1, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (2, 2, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (3, 3, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (4, 4, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (5, 5, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (6, 6, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (7, 7, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (8, 8, 'Fácil,Difícil');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (9, 9, 'Botones');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (10, 10, 'Abiertas,Cerradas');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (11, 11, '1-2-?, 8-9-?, 5-?-7, 6-7-?, 0-1-?, ?-3-4');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (12, 12, 'Manzanas,Uvas,Piñas,Naranjas,Peras');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (13, 13, 'Cuantas peras quedan?');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (14, 14, 'Camino');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (15, 15, 'Amigos');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (16, 16, 'Numero a la izquierda del pulpo,Número en la isla con la niña y el tesoro,Número debajo de las espadas');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (17, 17, 'Bichos');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (18, 18, 'Puzzle');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (19, 19, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (20, 20, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (21, 21, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (22, 22, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (23, 23, 'Figura');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (24, 24, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (25, 25, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (26, 26, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (27, 27, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (28, 28, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (29, 29, 'Rosa,Amarilla,Azul,Verde Claro,Naranja');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (30, 30, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (31, 31, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (32, 32, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (33, 33, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (34, 34, 'Solo,Grupo');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (35, 35, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (36, 36, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (37, 37, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (38, 38, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (39, 39, 'Clasifica');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (40, 40, 'Relaciona');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (41, 41, 'Organiza');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (42, 42, 'Si,No');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (43, 43, 'Si,No');")


                            //Insert into recomendaciones
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (1, 1, 1, 'Leer libros ilustrados en voz alta.\n" +
                                    "Jugar juegos de palabras como rimas o adivinanzas.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (2, 1, 2, 'Escribir diarios o cuentos cortos.\n" +
                                    "Participar en debates o discusiones grupales.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (3, 1, 3, 'Aprender un idioma extranjero.\n" +
                                    "Crear y presentar historias o poesía.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (4, 2, 1, 'Usar rompecabezas sencillos y juegos matemáticos básicos.\n" +
                                    "Practicar cálculos mentales con juegos interactivos.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (5, 2, 2, 'Resolver problemas matemáticos prácticos.\n" +
                                    "Introducir actividades de programación básica.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (6, 2, 3, 'Trabajar en proyectos de razonamiento avanzado, como el diseño de algoritmos.\n" +
                                    "Explorar estrategias de pensamiento lógico en ciencia.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (7, 3, 1, 'Jugar con bloques de construcción y maquetas.\n" +
                                    "Trabajar en actividades simples de dibujo.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (8, 3, 2, 'Crear mapas conceptuales o dibujos detallados.\n" +
                                    "Usar software de diseño simple, como herramientas de modelado 3D.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (9, 3, 3, 'Trabajar en proyectos avanzados de diseño arquitectónico o artístico.\n" +
                                    "Resolver puzzles complejos, como cubos de Rubik.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (10, 4, 1, 'Escuchar música y reproducir ritmos simples.\n" +
                                    "Participar en actividades básicas como golpear un tambor.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (11, 4, 2, 'Aprender un instrumento sencillo como flauta o teclado.\n" +
                                    "Explorar diferentes géneros musicales.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (12, 4, 3, 'Componer melodías o participar en ensambles musicales.\n" +
                                    "Estudiar teoría musical avanzada.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (13, 5, 1, 'Participar en juegos activos, como correr o saltar.\n" +
                                    "Realizar ejercicios básicos de coordinación.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (14, 5, 2, 'Practicar deportes simples o actividades físicas en grupo.\n" +
                                    "Aprender movimientos básicos de danza.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (15, 5, 3, 'Participar en deportes competitivos.\n" +
                                    "Trabajar en coreografías avanzadas o teatro físico.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (16, 6, 1, 'Jugar juegos de rol para mejorar habilidades sociales.\n" +
                                    "Participar en actividades grupales supervisadas.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (17, 6, 2, 'Resolver conflictos grupales con guía.\n" +
                                    "Realizar dinámicas de equipo, como proyectos colaborativos.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (18, 6, 3, 'Liderar actividades grupales.\n" +
                                    "Participar en mediación o enseñanza de habilidades sociales.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (19, 7, 1, 'Usar diarios para expresar pensamientos.\n" +
                                    "Realizar ejercicios básicos de autoevaluación.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (20, 7, 2, 'Participar en actividades que fomenten la reflexión personal.\n" +
                                    "Establecer metas pequeñas y alcanzables.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (21, 7, 3, 'Trabajar en proyectos de autoconocimiento profundo.\n" +
                                    "Ayudar a otros a reflexionar sobre sus metas.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (22, 8, 1, 'Observar animales o plantas locales.\n" +
                                    "Realizar caminatas en la naturaleza.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (23, 8, 2, 'Cultivar plantas o participar en proyectos de reciclaje.\n" +
                                    "Explorar documentales sobre naturaleza.');")
                            db.execSQL("INSERT INTO recomendaciones (id, categoriaId, clasificacionId, recomendacion) VALUES (24, 8, 3, 'Estudiar ecosistemas locales en profundidad.\n" +
                                    "Participar en proyectos científicos sobre medio ambiente.');")

                        }
                    })
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}
