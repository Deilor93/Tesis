package com.example.tesis.logica

import androidx.room.*

@Entity(tableName = "recomendaciones")
data class Recomendacion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoriaId: Int,
    val clasificacion: String,
    val recomendacion: String
)