package com.example.tesis.logica

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Categoria::class, Pregunta::class, Estudiante::class, Resultado::class, Recomendacion::class, Clasificacion::class, Respuesta::class], version = 3, exportSchema = false)

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
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId) VALUES (1, 'Soly quiere saber si tienes libros o revistas en casa', 'Si', 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId) VALUES (2, 'Cuentale a Soly si tienes instrumentos musicales en casa', 'Si', 4);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId) VALUES (3, 'Dile a Soly si tienes juguetes en casa', 'Si', 7);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId) VALUES (4, 'Disfrutas leyendo cuentos o libros?', 'Si', 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId) VALUES (5, 'Cuando juegas con amigos o amigas, cuentas o inventas historias para divertirse?', 'Si', 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId) VALUES (6, 'Te gustan las clases de español?', 'Si', 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId) VALUES (7, 'Te gusta hablar con amigos y familia?', 'Si', 1);")
                            db.execSQL("INSERT INTO preguntas (id, pregunta, respuestaCorrecta, categoriaId) VALUES (8, 'Leer te parece fácil o difícil?', 'Fácil', 1);")

                            // Insert into respuestas
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (1, 1, 'si,no');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (2, 2, 'si,no');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (3, 3, 'si,no');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (4, 4, 'si,no');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (5, 5, 'si,no');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (6, 6, 'si,no');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (7, 7, 'si,no');")
                            db.execSQL("INSERT INTO respuestas (id, preguntaId, respuesta) VALUES (8, 8, 'fácil,difícil');")
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
