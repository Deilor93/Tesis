package com.example.tesis.logica

import androidx.room.*


@Entity(tableName = "clasificaciones")
data class Clasificacion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String

)