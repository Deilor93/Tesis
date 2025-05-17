package com.example.tesis.vista.modelo

import androidx.compose.ui.geometry.Offset

//Data class para los items arrastrables de preguntas con item arrastrable
data class DraggableItem(
    val id: String,
    val imageResId: Int,
    var offset: Offset = Offset.Zero,
    var assignedCell: GridCell? = null
)
