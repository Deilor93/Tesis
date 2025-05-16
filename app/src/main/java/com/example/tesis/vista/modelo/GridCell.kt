package com.example.tesis.vista.modelo

import androidx.compose.ui.geometry.Offset

data class GridCell(
    val row: Int,
    val column: Int,
    var position: Offset,  // top-left position of the cell
    val index: Int         // unique index for easy mapping and saving
)
