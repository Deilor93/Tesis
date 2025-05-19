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
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (4, 'Disfrutas leyendo cuentos o libros?', 'Si', 1, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (5, 'Cuando juegas con amigos o amigas, cuentas o inventas historias para divertirse?', 'Si', 1, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (6, 'Te gustan las clases de español?', 'Si', 1, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (7, 'Te gusta hablar con amigos y familia?', 'Si', 1, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (8, 'Leer te parece fácil o difícil?', 'Fácil', 1, 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (9, 'Puedes decirle a Soly cuántos botones tiene la camisa?', '8', 2, 3);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (10, 'Ayuda a Soly a contar las latas, cuántas latas abiertas y cuántas latas cerradas hay?', '1,3', 2, 3);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (11, 'Soly quiere contar, dile a Soly cuál es el siguiente número en la secuencia', '3,8,10,2,6,2', 2, 3);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (12, 'Soly quiere tu ayuda para contar las frutas, cuántas puedes ver de cada una?', '6,2,4,5,4', 2, 3);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (13, 'Soly llevó 5 peras para compartir con su amigo, si se comieron 2, cuántas quedaron?', '2', 2, 3);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (14, 'Soly necesita tu ayuda para volver a casa, dibuja el camino que debe seguir para llegar', '32-33-34-35-28-21-14-7-12-6-0-1-2-3', 3, 2);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId, tipoPregunta) VALUES (15, 'Ayuda a los amigos de Soly a llegar a su casilla correspondiente siguiendo el ejemplo en la imagen', 'A:1-2,B:1-1,C:0-3,D:3-0,E:3-2,F:2-3', 3, 4);")

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
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (13, 13, 'Cuantas peras hay?');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (14, 14, 'Camino');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (15, 15, 'Amigos');")

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
