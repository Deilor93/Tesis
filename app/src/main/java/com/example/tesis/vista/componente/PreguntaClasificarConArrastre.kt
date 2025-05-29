package com.example.tesis.vista.component

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.tesis.vista.componente.AnimacionTexto
import com.example.tesis.vista.componente.ImagenArrastrable
import com.example.tesis.vista.modelo.DraggableItem
import com.example.tesis.vista.modelo.GridCell
import kotlin.math.roundToInt

@Composable
fun PreguntaClasificarConArrastre(
    context: Context,
    idPregunta: Int,
    preguntaText: String,
    items: List<DraggableItem>,
    gridSize: Int,
    gridLabels: List<String>,
    onAssignmentChanged: (List<Boolean>) -> Unit
) {
    val density = LocalDensity.current
    val cellSizeDp = 75.dp
    val cellSizePx = with(density) { cellSizeDp.toPx() }
    val totalGrids = gridLabels.size

    val gridCells = remember {
        val cells = mutableListOf<GridCell>()
        var index = 0
        for (gridIndex in 0 until totalGrids) {
            for (row in 0 until gridSize) {
                for (col in 0 until gridSize) {
                    val x = (gridIndex * gridSize + col) * cellSizePx
                    val y = row * cellSizePx
                    cells.add(GridCell(
                        index = index,
                        row = row,
                        column = (col + gridIndex * gridSize),
                        position = Offset(x, y)))
                }
            }
        }
        cells
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimacionTexto(preguntaText)
        Spacer(modifier = Modifier.height(16.dp))

        // Dynamic grid layout: each box fills equal space horizontally
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(cellSizeDp * gridSize),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            gridLabels.forEach { label ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = label)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawRect(
                                color = Color.Black,
                                style = Stroke(3f)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Initialize item positions
        LaunchedEffect(Unit) {
            items.forEachIndexed { index, item ->
                if (item.assignedCell == null) {
                    val row = index / gridSize
                    val col = totalGrids * gridSize + index % gridSize
                    val cell = gridCells.firstOrNull { it.row == row && it.column == col }
                    item.assignedCell = cell
                }
            }
            onAssignmentChanged(items.map { it.assignedCell?.column ?: -1 in 0 until (totalGrids * gridSize) })
        }

        // Draggable items on top of the grid layout
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(cellSizeDp * gridSize)
        ) {
            items.forEach { item ->
                ImagenArrastrable(
                    item = item,
                    gridCells = gridCells,
                    allItems = items,
                    initialOffset = item.assignedCell?.let {
                        Offset(
                            it.position.x + cellSizePx / 2 - 40f,
                            it.position.y + cellSizePx / 2 - 40f
                        )
                    } ?: Offset.Zero,
                    cellSize = cellSizePx,
                    gridWidth = totalGrids * gridSize * cellSizePx,
                    gridHeight = gridSize * cellSizePx,
                    onAssignmentChanged = onAssignmentChanged
                )
            }
        }
    }
}
