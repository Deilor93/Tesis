package com.example.tesis.vista.modelo

import com.example.tesis.logica.Clasificacion
import com.example.tesis.logica.Recomendacion

//Data class de clasificacion y recomendacion para asignar por categoria
data class ClasificacionConRecomendacion(
    val nombre: String,
    val puntuacion: Int,
    val clasificacion: Clasificacion?,
    val recomendacion: Recomendacion?
)