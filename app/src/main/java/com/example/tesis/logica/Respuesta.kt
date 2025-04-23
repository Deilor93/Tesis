package com.example.tesis.logica

import androidx.room.*


@Entity(tableName = "respuestas",
    foreignKeys = [
        ForeignKey(
            entity = Estudiante::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("estudianteId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Clasificacion::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("clasificacionId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
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
    val estudianteId: Int,
    val preguntaId: Int,
    val clasificacionId: Int,
    val respuesta: String,
    val fecha: String
)