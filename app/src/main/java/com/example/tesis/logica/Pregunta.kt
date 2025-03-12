package com.example.tesis.logica

import androidx.room.*



@Entity(tableName = "preguntas")
data class Pregunta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pregunta: String,
    val respuestaCorrecta: String,
    val categoriaId: Int
)