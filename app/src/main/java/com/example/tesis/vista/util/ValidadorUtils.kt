package com.example.tesis.vista.util

class ValidadorUtils {
    /**
     * Valida el patron ingresado por el usuario, tomando en cuenta que sea igual.
     * Formato de ejemplo: "41-40-39-34"
     */
    fun isPatronCorrecto(userPattern: String, correctPattern: String): Boolean {
        return userPattern == correctPattern
    }

    /**
     * Valida el resultado ingresado por el usuario en el juego de arrastrar y ubicar.
     * El orden y posicion se toman en la cuenta
     * Formato de ejemplo: "A:0-0;B:1-2;C:3-3"
     */
    fun isArrastrarYUbicarCorrecto(userResult: String, correctResult: String): Boolean {
        return userResult == correctResult
    }

    /**
     * Parsea el string resultado del juego de arrastrar en un mapa de idObjeto -> posicion (fila-columna)
     * Ejemplo: "A:0-0;B:1-2" -> mapOf("A" to "0-0", "B" to "1-2")
     * Objeto A -> posicion 0-0, Objeto B -> posicion 1-2
     */
    fun parseDragResult(result: String): Map<String, String> {
        return result.split(";")
            .mapNotNull {
                val parts = it.split(":")
                if (parts.size == 2) parts[0] to parts[1] else null
            }
            .toMap()
    }

    /**
     * Extra utility: Checks if all placed items are within allowed grid bounds
     */
    fun validarPosicionesArrastre(result: String, maxRow: Int, maxCol: Int): Boolean {
        return parseDragResult(result).values.all { pos ->
            val coords = pos.split("-").mapNotNull { it.toIntOrNull() }
            coords.size == 2 && coords[0] in 0 until maxRow && coords[1] in 0 until maxCol
        }
    }
}