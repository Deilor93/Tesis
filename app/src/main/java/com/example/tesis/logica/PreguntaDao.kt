package com.example.tesis.logica

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PreguntaDao {

    data class preguntatxt(
        @ColumnInfo(name = "pregunta") val pregunta: String?
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPregunta(pregunta: Pregunta)

    @Query("SELECT * FROM preguntas WHERE categoriaId = :categoriaId")
    fun getPreguntasByCategoria(categoriaId: Int): List<Pregunta>

    @Query("SELECT * FROM preguntas WHERE id = :id")
    fun getPreguntaById(id: Int): Pregunta?

    @Query("SELECT pregunta FROM preguntas WHERE id = :id")
    fun getTextoPreguntaById(id: Int): String

    @Query("SELECT COUNT(id) FROM preguntas")
    fun getCantidadPreguntas(): Int
}