package com.example.tesis.vista.componente

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.tesis.vista.modelo.DraggableItem
import com.example.tesis.vista.modelo.GridCell
import kotlin.math.hypot
import kotlin.math.roundToInt

@Composable
fun ImagenArrastrable(
    item: DraggableItem,
    gridCells: List<GridCell>,
    allItems: List<DraggableItem>,
    initialOffset: Offset,
    cellSize: Float,
    visibleGridSize: Int,
    onAssignmentChanged: (List<Boolean>) -> Unit
) {
    // El offset debe ser persistente incluso si el composable se re compone
    var offset by remember { mutableStateOf(initialOffset) }

    // Asegura actualizacion del offset durante la composicion del composable
    LaunchedEffect(item.assignedCell) {
        item.assignedCell?.let { cell ->
            offset = Offset(
                cell.position.x + (cellSize / 2) - 60f,
                cell.position.y + (cellSize / 2) - 60f
            )
        }
    }

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .size(80.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        offset += dragAmount
                    },
                    onDragEnd = {
                        //Limita las casillas disponibles a las casillas visibles y las casillas desocupadas
                        val availableCells = gridCells.filter { cell ->
                            cell.row < visibleGridSize &&/*Previene que los items se pongan en
                                                            la fila invisible despues de moverlos*/
                            cell.column < visibleGridSize &&/*Previene que los items se pongan
                                                                en lsa columnas invisibles*/
                            allItems.none { it.assignedCell?.index == cell.index } /*Previene que los
                                                        items se puedan sobrelapar en la misma celda*/
                        }

                        // Busca la celda vacia mas cercana delimitado por las restricciones anteriores
                        val nearestCell = availableCells.minByOrNull { cell ->
                            hypot(
                                (cell.position.x + cellSize / 2) - (offset.x + 60f),
                                (cell.position.y + cellSize / 2) - (offset.y + 60f)
                            )
                        }

                        nearestCell?.let { cell ->
                            item.assignedCell = cell
                            offset = Offset(
                                cell.position.x + (cellSize / 2) - 60f,
                                cell.position.y + (cellSize / 2) - 60f
                            )
                        }

                        onAssignmentChanged(
                            allItems.map {
                                it.assignedCell?.let { cell ->
                                    cell.row in 0 until visibleGridSize && cell.column in 0 until visibleGridSize
                                } == true
                            }
                        )
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = item.imageResId),
            contentDescription = item.id,
            modifier = Modifier.size(80.dp)
        )
    }
}
