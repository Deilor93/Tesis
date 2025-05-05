package com.example.tesis.logica

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoriaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoria(categoria: Categoria)

    @Query("SELECT * FROM categorias WHERE id = :id")
    fun getCategoriaById(id: Int): Categoria?

    @Query("SELECT * FROM categorias")
    fun getAllCategorias(): List<Categoria>
}