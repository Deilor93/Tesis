package com.example.tesis.logica

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface RespuestaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRespuesta(respuesta: Respuesta)

    @Query("SELECT respuesta FROM respuestas WHERE preguntaId = :preguntaId")
    fun getRespuestaByPregunta(preguntaId: Int): String

    @Query("SELECT respuesta FROM respuestas WHERE id=:id")
    fun getRespuestaById(id: Int): String

}