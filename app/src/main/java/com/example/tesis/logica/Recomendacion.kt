package com.example.tesis.logica

import androidx.room.*

@Entity(tableName = "recomendaciones",
    foreignKeys = [
        ForeignKey(
            entity = Categoria::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("categoriaId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Clasificacion::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("clasificacionId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE)
    ])
data class Recomendacion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val categoriaId: Int,
    val clasificacionId: Int,
    val recomendacion: String
)