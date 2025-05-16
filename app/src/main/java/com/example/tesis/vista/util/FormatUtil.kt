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
    /**
     * Parser para los strings con formato "A:1-2,B:0-3,C:null"
     * Retorna un Map donde la llave es le ID del objeto y el valor es un par de fila y columna
     * si no fue asignado es null.
     */
    fun parseDragMatchResult(result: String): Map<String, Pair<Int, Int>?> {
        return result.split(",").mapNotNull { entry ->
            val parts = entry.split(":")
            if (parts.size == 2) {
                val id = parts[0]
                val positionPart = parts[1]
                val position = if (positionPart == "null") {
                    null
                } else {
                    val coords = positionPart.split("-")
                    if (coords.size == 2) {
                        Pair(coords[0].toIntOrNull() ?: -1, coords[1].toIntOrNull() ?: -1)
                    } else null
                }
                id to position
            } else null
        }.toMap()
    }
}
