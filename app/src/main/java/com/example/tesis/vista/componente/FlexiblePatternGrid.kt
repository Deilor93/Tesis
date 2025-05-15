package com.example.tesis.vista.componente

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.hypot

//Patron para pregunta copiar imagen
@Composable
fun FlexiblePatternGrid(
    gridSize: Int = 3,
    initialPattern: String,
    onPatternCompleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedDots = remember { mutableStateListOf<Int>() }
    val dotCenters = remember { mutableStateListOf<Offset>() }
    val dotRadius = 30f

    LaunchedEffect(initialPattern) {
        selectedDots.clear()
        if (initialPattern.isNotBlank()) {
            selectedDots.addAll(initialPattern.split("-").mapNotNull { it.toIntOrNull() })
        }
    }

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
        Canvas(modifier = Modifier.fillMaxSize()) {
            dotCenters.clear()
            val spacingX = size.width / (gridSize + 1)
            val spacingY = size.height / (gridSize + 1)

            for (i in 0 until gridSize) {
                for (j in 0 until gridSize) {
                    val x = spacingX * (j + 1)
                    val y = spacingY * (i + 1)
                    val center = Offset(x, y)
                    dotCenters.add(center)

                    drawCircle(
                        color = if (selectedDots.contains(i * gridSize + j)) Color(0xFF55DFBE) else Color.LightGray,
                        radius = dotRadius,
                        center = center,
                        style = Stroke(width = 4f)
                    )
                }
            }

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