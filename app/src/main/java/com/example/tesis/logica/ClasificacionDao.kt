package com.example.tesis.logica


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ClasificacionDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClasificacion(clasificacion: Clasificacion)

    @Query("SELECT * FROM clasificaciones WHERE id = :id")
    suspend fun getClasificacionById(id: Int): List<Clasificacion>

    @Query("SELECT * FROM clasificaciones")
    fun getAllClasificaciones(): List<Clasificacion>

}