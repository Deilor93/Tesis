package com.example.tesis.logica


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RecomendacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecomendacion(recomendacion: Recomendacion)

    //@Query("SELECT * FROM estudiantes WHERE id = :id")
    //suspend fun getRecomendiacionById(id: Int): List<Recomendacion>

    @Query("SELECT * FROM recomendaciones")
    fun getAllRecomendaciones(): List<Recomendacion>

    @Query("SELECT * FROM recomendaciones WHERE categoriaId = :categoriaId")
    fun getRecomendacionesByCategoria(categoriaId: Int): List<Recomendacion>
}