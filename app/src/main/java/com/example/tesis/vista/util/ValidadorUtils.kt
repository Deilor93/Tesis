package com.example.tesis.vista.util

import kotlin.math.absoluteValue

//Utilidades para validacion de datos
class ValidadorUtils {

    /**
     * Valida el patron ingresado por el usuario, tomando en cuenta que sea igual.
     * Formato de ejemplo: "41-40-39-34"
     */
    fun isPatronCorrecto(userPattern: String, correctPattern: String): Boolean {
        return userPattern == correctPattern
    }

    /**
     * Parsea el string resultado del juego de arrastrar en un mapa de idObjeto -> posicion (fila-columna)
     * Ejemplo: "A:0-0;B:1-2" -> mapOf("A" to "0-0", "B" to "1-2")
     * Objeto A -> posicion 0-0, Objeto B -> posicion 1-2
     */
    fun parseDragResult(result: String): Map<String, String> {
        return result.split(",")
            .mapNotNull {
                val parts = it.split(":")
                if (parts.size == 2) parts[0] to parts[1] else null
            }
            .toMap()
    }

    /**
     * Valida el resultado ingresado por el usuario en el juego de arrastrar y ubicar.
     * El orden y posicion se toman en la cuenta
     * Formato de ejemplo: "A:0-0;B:1-2;C:3-3"
     */
    fun isArrastrarYUbicarCorrecto(userResult: String, correctResult: String): Boolean {
        val userMap = parseDragResult(userResult)
        val correctMap = parseDragResult(correctResult)
        // Revisa que los items en la respuesta correcta tengan la misma posicion a la respuesta dada
        return correctMap.all { (id, expectedPos) ->
            userMap[id] == expectedPos
        }
    }

    /**
     * Verifica si al menos [minimo] items están en la posición correcta.
     * Ejemplo: "A:0-0,B:1-1,C:2-2" debe coincidir exactamente con el valor de posicion "B:1-1,A:0-0,C:2-2"
     */
    fun isMinimoItemsCorrectos(userResult: String, correctResult: String, minimo: Int): Boolean {
        val userMap = parseDragResult(userResult)
        val correctMap = parseDragResult(correctResult)

        val countCorrect = userMap.count { (id, userPos) ->
            correctMap[id] == userPos
        }
        return countCorrect >= minimo
    }

    /**
     * Valida si dos cadenas de texto son equivalentes considerando solo las palabras:
     * - Ignora mayúsculas/minúsculas
     * - Ignora signos de puntuación
     * - Ignora espacios en blanco al inicio y final
     */
    fun compararSoloTexto(userInput: String, correctAnswer: String): Boolean {
        fun normalize(text: String): String {
            return text
                .trim()
                .lowercase()
                .replace(Regex("[\\p{Punct}]"), "") // Elimina puntuación
                .replace("\\s+".toRegex(), " ") // Normaliza espacios múltiples
        }
        return normalize(userInput) == normalize(correctAnswer)
    }

    /**
     * Valida si dos cadenas de texto son equivalentes considerando solo las palabras:
     * - Ignora mayúsculas/minúsculas
     * - Ignora signos de puntuación
     * - Ignora espacios en blanco al inicio y final
     */
    fun isCorrectPlacement(userMap: Map<String, String>, correctMap: Map<String, String>, validatePosition: Boolean = false): Boolean {
        return userMap.all { (id, value) ->
            val correctValue = correctMap[id]
            if (!validatePosition) {
                value.split(":")[0] == correctValue?.split(":")?.get(0)
            } else {
                // Validate position with some tolerance
                val userCoords = value.split(":").getOrNull(1)?.split("-")?.mapNotNull { it.toIntOrNull() }
                val correctCoords = correctValue?.split(":")?.getOrNull(1)?.split("-")?.mapNotNull { it.toIntOrNull() }
                userCoords != null && correctCoords != null &&
                        (userCoords[0] - correctCoords[0]).absoluteValue <= 10 &&
                        (userCoords[1] - correctCoords[1]).absoluteValue <= 10
            }
        }
    }

}