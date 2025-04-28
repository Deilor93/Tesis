package com.example.tesis.logica

import androidx.room.*


@Entity(tableName = "respuestas",
    foreignKeys = [
        ForeignKey(
            entity = Pregunta::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("preguntaId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Respuesta(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val preguntaId: Int,
    val respuesta: String
)