package com.example.tesis.logica

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface RespuestaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResultado(resultado: Resultado)

    @Query("SELECT * FROM resultados WHERE estudianteId = :participanteId")
    fun getResultadosByParticipante(participanteId: Int): List<Resultado>
}