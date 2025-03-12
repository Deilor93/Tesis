package com.example.tesis.logica

import androidx.room.*



@Entity(tableName = "preguntas")
data class Pregunta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pregunta: String,
    val respuestaCorrecta: String,
    val categoriaId: Int
)

@Entity(tableName = "categorias")
data class Categoria(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String
)

@Entity(tableName = "resultados")
data class Resultado(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val participanteId: Int,
    val preguntaId: Int,
    val respuesta: String,
    val fecha: String
)

@Entity(tableName = "recomendaciones")
data class Recomendacion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoriaId: Int,
    val clasificacion: String,
    val recomendacion: String
)
