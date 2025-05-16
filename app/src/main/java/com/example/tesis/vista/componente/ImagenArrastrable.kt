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
    cellSize: Float
) {
    // Offset must be persistent even if the Composable recomposes, use rememberUpdatedState pattern
    var offset by remember { mutableStateOf(initialOffset) }

    // Ensure offset updates when the item has an assignedCell during composition
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
                        // Find nearest unoccupied cell
                        val availableCells = gridCells.filter { cell ->
                            allItems.none { it.assignedCell?.index == cell.index }
                        }

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
