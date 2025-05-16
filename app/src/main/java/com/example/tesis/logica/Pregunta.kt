package com.example.tesis.logica

import androidx.room.*



@Entity(tableName = "preguntas",
    foreignKeys = [
        ForeignKey(
        entity = Categoria::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("categoriaId"),
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Pregunta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pregunta: String,
    val respuestaCorrecta: String,
    val categoriaId: Int,
    val tipoPregunta: Int
)