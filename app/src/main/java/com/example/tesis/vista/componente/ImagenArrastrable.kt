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
fun ImagenArrastrable(
    item: DraggableItem,
    gridCells: List<GridCell>,
    allItems: List<DraggableItem>,
    initialOffset: Offset,
    cellWidthPx: Float,
    cellHeightPx: Float,
    onAssignmentChanged: (List<Boolean>) -> Unit
) {
    var offset by remember { mutableStateOf(initialOffset) }



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
                        val nearestCell = gridCells.minByOrNull { cell ->
                            hypot(
                                (cell.position.x + cellWidthPx / 2) - (offset.x + 40f),
                                (cell.position.y + cellHeightPx / 2) - (offset.y + 40f)
                            )
                        }

                        nearestCell?.let { cell ->

                            item.assignedCell = cell
                            item.assignedLabel = cell.label

                        }



                        onAssignmentChanged(allItems.map { it.assignedCell != null })
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.size(70.dp),
            painter = painterResource(id = item.imageResId),
            contentDescription = item.id
        )
    }
}
