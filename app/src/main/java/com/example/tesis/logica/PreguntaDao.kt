package com.example.tesis.logica

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PreguntaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPregunta(pregunta: Pregunta)

    @Query("SELECT * FROM preguntas WHERE categoriaId = :categoriaId")
    fun getPreguntasByCategoria(categoriaId: Int): List<Pregunta>
}