package com.example.tesis.vista.componente

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.tesis.vista.modelo.DraggableItem
import com.example.tesis.vista.modelo.GridCell
import kotlin.math.hypot
import kotlin.math.roundToInt

@Composable
fun ImagenArrastrableSnapping(
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
                        /*Asigna el area valida para ubicar los objetos, unicamente dentro del
                        grid visible*/
                        val gridAreaCells = gridCells.filter { cell ->
                            cell.row < visibleGridSize && cell.column < visibleGridSize
                        }
                        /*Busca la casilla vacia mas cercana para ubicar y fijar el objeto*/
                        val nearestCell = gridAreaCells.minByOrNull { cell ->
                            hypot(
                                (cell.position.x + cellSize / 2) - (offset.x + 60f),
                                (cell.position.y + cellSize / 2) - (offset.y + 60f)
                            )
                        }
                        /*Valida la casilla del grid donde se va a ubicar el objeto al soltarlo*/
                        nearestCell?.let { cell ->
                            /*Valida los objetos que ya estan ocupando celdas y que no sea el
                            * que se esta moviendo*/
                            val occupyingItem = allItems.firstOrNull {
                                it != item && it.assignedCell?.index == cell.index
                            }
                            /*Valid asi el objeto movido estaba dentro del grid o fuera del grid*/
                            val itemWasInGrid = item.assignedCell?.row in 0 until visibleGridSize &&
                                    item.assignedCell?.column in 0 until visibleGridSize
                            /*Valida si la celda objetivo esta dentro del grid o fuera del grid*/
                            val targetIsInGrid = cell.row in 0 until visibleGridSize &&
                                    cell.column in 0 until visibleGridSize

                            when {
                                /* Caso1: realiza el cambio de posicion de los objetos cuando
                                * el objeto movido se deja en una celda ya ocupada siempre y cuando
                                * el objeto movido ya se econtrase dentro del grid*/
                                occupyingItem != null && itemWasInGrid && targetIsInGrid -> {
                                    val temp = occupyingItem.assignedCell
                                    occupyingItem.assignedCell = item.assignedCell
                                    item.assignedCell = temp
                                    Log.d("item movido", item.toString())
                                    Log.d("item ocupante", occupyingItem.toString())
                                    Log.d("item temp", temp.toString())
                                }

                                // Caso 2: Mueve el objeto a una casilla vacia
                                occupyingItem == null -> {
                                    item.assignedCell = cell
                                }

                                // Case 3: El objeto regresa a la casilla vacia mas cercana
                                else -> {
                                    // No change in assignedCell
                                }
                            }

                            // Ubica la imagen visualmente dentro de la casilla asignada
                            item.assignedCell?.let {
                                offset = Offset(
                                    it.position.x + (cellSize / 2) - 60f,
                                    it.position.y + (cellSize / 2) - 60f
                                )
                            }
                        }

                        // Actualiza el valor de la ubicacion de los objetos una vez se hizo el cambio de celda
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
            modifier = Modifier.size(70.dp)
                .graphicsLayer{
                    rotationZ = item.rotation
                    scaleX = if (item.flipH) -1f else 1f
                    scaleY = if (item.flipV) -1f else 1f
                },
            painter = painterResource(id = item.imageResId),
            contentDescription = item.id
        )
    }
}
