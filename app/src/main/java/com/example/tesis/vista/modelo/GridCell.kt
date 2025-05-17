package com.example.tesis.vista.modelo

import androidx.compose.ui.geometry.Offset

//Data class de las celdas utilizadas para crear los grids para preguntas de objetos arrastrables
data class GridCell(
    val row: Int,
    val column: Int,
    var position: Offset,  // Posicion superior izquierda de la celda
    val index: Int         // Indice unico para mapeo y guardado
)
