package com.example.tesis.vista.util

import com.example.tesis.vista.modelo.DraggableItem

object FormatUtil {
    /**
     * Serializa los draggable items en un string como "A:1-2,B:0-3,C:2-1". Para compararlo con el resultado correcto
     * Si el item no es ubicado se guarda como "A:null".
     */
    fun formatDragMatchResult(items: List<DraggableItem>): String {
        return items.joinToString(",") { item ->
            val assigned = item.assignedCell
            if (assigned != null) {
                "${item.id}:${assigned.row}-${assigned.column}"
            } else {
                "${item.id}:null"
            }
        }
    }

    fun formatDragPlaceResult(items: List<DraggableItem>): String {
        return items.joinToString(",") { item ->
            val label = item.assignedCell?.label ?: "Ninguno"
            "${item.id}:$label"
        }
    }
}
