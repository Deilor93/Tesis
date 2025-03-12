package com.example.tesis.logica

import androidx.room.*


@Entity(tableName = "resultados")
data class Resultado(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val participanteId: Int,
    val preguntaId: Int,
    val respuesta: String,
    val fecha: String
)