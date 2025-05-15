package com.example.tesis.vista.modelo

import androidx.compose.ui.geometry.Offset

data class GridCell(
    val row: Int,
    val column: Int,
    val position: Offset
)
