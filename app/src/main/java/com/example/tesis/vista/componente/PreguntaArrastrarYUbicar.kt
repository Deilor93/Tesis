package com.example.tesis.vista.component

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.tesis.vista.componente.AnimacionTexto
import com.example.tesis.vista.componente.ImagenArrastrable
import com.example.tesis.vista.modelo.DraggableItem
import com.example.tesis.vista.modelo.GridCell
import com.example.tesis.vista.util.ImagenPregunta

@Composable
fun PreguntaArrastrarYUbicar(
    context: Context,
    idPregunta: Int,
    preguntaText: String,
    items: List<DraggableItem>,
    gridSize: Int,
    onPositionsSaved: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimacionTexto(preguntaText)

        Spacer(modifier = Modifier.height(16.dp))

        val cellSizePx = 100f
        val gridCells = remember {
            val cells = mutableListOf<GridCell>()
            var index = 0
            for (row in 0 until gridSize) {
                for (col in 0 until gridSize) {
                    val x = col * cellSizePx
                    val y = row * cellSizePx
                    cells.add(GridCell(index = index, row = row, column = col, position = Offset(x, y)))
                    index++
                }
            }
            cells
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ImagenPregunta(
                context = context,
                idPregunta = idPregunta,
                modifier = Modifier
                    .size(300.dp)
                    .padding(vertical = 16.dp)
            )

            // Grid area with Canvas and items over it
            Box(
                modifier = Modifier
                    .size((gridSize * cellSizePx).dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    gridCells.forEach { cell ->
                        drawRect(
                            color = Color.Black,
                            topLeft = cell.position,
                            size = Size(cellSizePx, cellSizePx),
                            style = Stroke(width = 2f)
                        )
                    }
                }

                // Draggable items over the grid
                items.forEach { item ->
                    ImagenArrastrable(
                        item = item,
                        gridCells = gridCells,
                        allItems = items,
                        // If not assigned, stay at the initial external position below the grid
                        initialOffset = item.assignedCell?.let {
                            Offset(it.position.x + cellSizePx / 2 - 40f, it.position.y + cellSizePx / 2 - 40f)
                        } ?: Offset(0f, (gridSize * cellSizePx + 100f))
                        ,
                        cellSize = cellSizePx
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Row showing the draggable items at the bottom if not assigned
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.filter { it.assignedCell == null }.forEach { item ->
                Column(modifier = Modifier.weight(1f)){
                    ImagenArrastrable(
                        item = item,
                        gridCells = gridCells,
                        allItems = items,
                        initialOffset = Offset(0f, (gridSize * cellSizePx + 100f)),
                        cellSize = cellSizePx
                    )
                }
            }
        }
    }
}

