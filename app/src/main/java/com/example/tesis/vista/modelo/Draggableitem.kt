package com.example.tesis.vista.modelo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

data class DraggableItem(
    val id: String,
    val imageResId: Int,
    var offset: Offset = Offset.Zero,
    var assignedLabel: String? = null,
    val correctLabel: String? = null,
    var relativePosition: Offset = Offset.Zero,
    val rotation: Float = 0f,
    val flipH: Boolean = false,
    val flipV: Boolean = false
) {
    var assignedCell by mutableStateOf<GridCell?>(null)
}
