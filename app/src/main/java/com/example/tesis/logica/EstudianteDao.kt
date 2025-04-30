package com.example.tesis.logica


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EstudianteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstudiante(estudiante: Estudiante):Long

    @Query("SELECT * FROM estudiantes WHERE id = :id")
    suspend fun getEstudianteById(id: Int): Estudiante?

    @Query("SELECT * FROM estudiantes")
    fun getAllParticipantes(): List<Estudiante>




}