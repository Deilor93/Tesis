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
fun ImagenArrastrable(
    item: DraggableItem,
    gridCells: List<GridCell>,
    allItems: List<DraggableItem>,
    initialOffset: Offset,
    cellSize: Float,
    gridWidth: Float,
    gridHeight: Float,
    onAssignmentChanged: (List<Boolean>) -> Unit
) {
    var offset by remember { mutableStateOf(initialOffset) }

    LaunchedEffect(item.assignedCell) {
        item.assignedCell?.let { cell ->
            offset = Offset(
                cell.position.x + cellSize / 2 - 40f,
                cell.position.y + cellSize / 2 - 40f
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
                        val newOffset = offset + dragAmount
                        offset = Offset(
                            newOffset.x.coerceIn(0f, gridWidth - 80f),
                            newOffset.y.coerceIn(0f, gridHeight - 80f)
                        )
                    },
                    onDragEnd = {
                        val nearestCell = gridCells.minByOrNull { cell ->
                            hypot(
                                (cell.position.x + cellSize / 2) - (offset.x + 40f),
                                (cell.position.y + cellSize / 2) - (offset.y + 40f)
                            )
                        }

                        nearestCell?.let { cell ->
                            item.assignedCell = cell
                            offset = Offset(
                                cell.position.x + (cellSize / 2) - 40f,
                                cell.position.y + (cellSize / 2) - 40f
                            )
                        }

                        onAssignmentChanged(
                            allItems.map { it.assignedCell?.let { c -> c.column in 0..Int.MAX_VALUE } == true }
                        )
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = item.imageResId),
            contentDescription = item.id,
            modifier = Modifier.size(70.dp)
        )
    }
}
