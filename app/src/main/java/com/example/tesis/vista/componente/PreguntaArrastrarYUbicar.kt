package com.example.tesis.vista.componente

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
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
    onAssignmentChanged: (List<Boolean>) -> Unit

) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimacionTexto(preguntaText)

        Spacer(modifier = Modifier.height(16.dp))

        val cellSizeDp = 75.dp
        val cellSizePx = with(LocalDensity.current) { cellSizeDp.toPx() }

        val visibleGridSize = gridSize
        val totalRows = gridSize
        val totalColumns = gridSize * 2 //Columnas extra invisibles para los items fuera del grid

        //Crea el grid de celdas sobre el cual se van a poner los items
        val gridCells = remember {
            val cells = mutableListOf<GridCell>()
            var index = 0
            for (row in 0 until totalRows) {
                for (col in 0 until totalColumns) {
                    val x = col * cellSizePx
                    val y = row * cellSizePx
                    cells.add(
                        GridCell(
                            index = index,
                            row = row,
                            column = col,
                            position = Offset(x, y)
                        )
                    )
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

            Box(
                modifier = Modifier
                    .size((gridSize * cellSizePx).dp)
            ) {
                //Dibuja el grid creado sobre la pantalla
                Canvas(modifier = Modifier.fillMaxSize()) {
                    gridCells.filter { it.row < visibleGridSize && it.column < gridSize }.forEach { cell ->
                    drawRect(
                            color = Color.Black,
                            topLeft = cell.position,
                            size = Size(cellSizePx, cellSizePx),
                            style = Stroke(width = 2f)
                        )
                    }
                }

                /**Inicializa la posicion de los items dentro de la fila invisible fuera del grid para que
                 * aparezcan alineados uno al lado del otro*/
                LaunchedEffect(Unit) {
                    items.forEachIndexed { index, item ->
                        if (item.assignedCell == null) {
                            val row = index / gridSize
                            val col = gridSize + index % gridSize

                            val cell = gridCells.firstOrNull { it.row == row && it.column == col }
                            item.assignedCell = cell
                            onAssignmentChanged(
                                items.map { it.assignedCell?.let { c ->
                                    c.row in 0 until gridSize && c.column in 0 until gridSize
                                } == true }
                            )
                        }
                    }
                }

                // Crea los tems arrastrables sobre el grid invsible
                items.forEach { item ->
                    ImagenArrastrableSnapping(
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
                        visibleGridSize = gridSize,
                        onAssignmentChanged = onAssignmentChanged
                    )
                }

            }
        }
    }
}