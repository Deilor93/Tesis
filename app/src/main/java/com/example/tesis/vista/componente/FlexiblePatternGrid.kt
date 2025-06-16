package com.example.tesis.vista.componente

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.hypot

@Composable
fun FlexiblePatternGrid(
    rows: Int,
    columns: Int,
    initialPattern: String,
    onPatternCompleted: (String) -> Unit,
    modifier: Modifier = Modifier,
    dotRadius: Float = 15f  //Radio para el tamaño de los puntos
) {
    val selectedDots = remember { mutableStateListOf<Int>() }
    val dotCenters = remember { mutableStateListOf<Offset>() }

    LaunchedEffect(initialPattern) {
        selectedDots.clear()
        if (initialPattern.isNotBlank()) {
            selectedDots.addAll(initialPattern.split("-").mapNotNull { it.toIntOrNull() })
        }
    }
    /**pointerInput detecta si se han seleccionado puntos, cuales son y en que orden,
    y lo asigna a la variable a guardar en la db*/
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { offset ->
                        checkDotSelection(offset, dotCenters, selectedDots, dotRadius)
                    },
                    onDrag = { change, _ ->
                        checkDotSelection(change.position, dotCenters, selectedDots, dotRadius)
                    },
                    onDragEnd = {
                        if (selectedDots.isNotEmpty()) {
                            val patternString = selectedDots.joinToString("-")
                            onPatternCompleted(patternString)
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxHeight().fillMaxWidth()) {
            dotCenters.clear()
            /**rows es 7 columnas es 6, se fijó el valor por el requerimiento de la pregunta pero el
             *  codigo sigue siendo dinamico
            */
            val marginXFactor = 0.25f
            val marginYFactor = 0.05f
            //Calculo de distancia entre puntos en el eje horizontal
            val spacingX = size.width * (1 - marginXFactor * 2) / (6 - 1)
            //Calculo de distancia entre puntos en el eje vertical
            val spacingY = size.height * (1 - marginYFactor * 2) / (7 - 1)

            for (i in 0 until 7) {
                for (j in 0 until 6) {
                    val x = size.width * marginXFactor + j * spacingX
                    val y = size.height * marginYFactor + i * spacingY
                    //Asignacion de centro del punto
                    val center = Offset(x, y)
                    dotCenters.add(center)
                    //Dibuja los "puntos" seleccionables para el patron y pinta los puntos seleccionados
                    drawCircle(
                        color = if (selectedDots.contains(i * 6 + j)) Color(0xFF55DFBE) else Color.LightGray,
                        radius = dotRadius,
                        center = center,
                        style = Stroke(width = 4f)
                    )
                }
            }
            //dibuja el "camino" entre los puntos seleccionados
            if (selectedDots.size >= 2) {
                val path = Path().apply {
                    selectedDots.map { dotCenters[it] }.forEachIndexed { index, offset ->
                        if (index == 0) moveTo(offset.x, offset.y)
                        else lineTo(offset.x, offset.y)
                    }
                }
                drawPath(path, color = Color(0xFF55DFBE), style = Stroke(width = 8f))
            }
        }
    }
}

//Revisa los puntos que han sido seleccionados para agregarlos al string a guardar
private fun checkDotSelection(
    position: Offset,
    dotCenters: List<Offset>,
    selectedDots: MutableList<Int>,
    dotRadius: Float,
    toleranceFactor: Float = 1.5f
) {
    dotCenters.forEachIndexed { index, center ->
        if (!selectedDots.contains(index)) {
            val distance = hypot(center.x - position.x, center.y - position.y)
            if (distance <= dotRadius * toleranceFactor) {
                selectedDots.add(index)
            }
        }
    }
}