package com.example.tesis.logica

import android.content.Context
import androidx.room.*

@Database(entities = [Categoria::class, Pregunta::class, Estudiante::class, Resultado::class, Recomendacion::class, Clasificacion::class], version = 1, exportSchema = false)

abstract class DbClass : RoomDatabase() {
    abstract fun categoriaDao(): CategoriaDao
    abstract fun preguntaDao(): PreguntaDao
    abstract fun estudianteDao(): EstudianteDao
    abstract fun resultadoDao(): ResultadoDao
    abstract fun recomendacionDao(): RecomendacionDao
    abstract fun clasificacionDao(): ClasificacionDao

    companion object {
        @Volatile private var INSTANCE: DbClass? = null

        fun getDatabase(context: Context): DbClass {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DbClass::class.java,
                    "soly_db"
                ).fallbackToDestructiveMigration()
                .createFromAsset("soly_db.db")
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
