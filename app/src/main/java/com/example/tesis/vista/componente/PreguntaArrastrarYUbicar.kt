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
        val visibleGridSize = gridSize
        val totalRows = gridSize + 1 // Agrega una fila extra invisible para los items fuera del grid
        val totalColumns = maxOf(gridSize, items.size) //Columnas extra invisibles para los items fuera del grid

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
                            val row = visibleGridSize
                            val col = index // Permite que se repartan por toda la fila
                            val cell = gridCells.firstOrNull { it.row == row && it.column == col }
                            item.assignedCell = cell
                        }
                    }
                }

                // Crea los tems arrastrables sobre el grid en sus celdas asignadas
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
                        visibleGridSize = gridSize
                    )
                }

            }
        }
    }
}

