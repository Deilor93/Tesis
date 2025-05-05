package com.example.tesis.logica

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ResultadoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResultado(resultado: Resultado):Long

    @Query("SELECT * FROM resultados WHERE id = :id")
    fun getResultadoById(id: Int): Resultado?
}
