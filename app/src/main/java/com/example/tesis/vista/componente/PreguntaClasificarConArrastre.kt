package com.example.tesis.vista.componente

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.example.tesis.vista.modelo.DraggableItem
import com.example.tesis.vista.modelo.GridCell

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PreguntaClasificarConArrastre(
    context: Context,
    idPregunta: Int,
    preguntaText: String,
    items: List<DraggableItem>,
    containerLabels: List<String>,
    cellImages: List<DraggableItem>? = null,
    onAssignmentChanged: (List<String>) -> Unit
) {
    val gridCells = remember { mutableStateListOf<GridCell>() }
    val density = LocalDensity.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimacionTexto(preguntaText)
        Spacer(modifier = Modifier.height(16.dp))

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Enough height for both cells + items
        ) {
            val cellWidthPx = constraints.maxWidth.toFloat() / containerLabels.size
            val cellWidthDp = with(density) { cellWidthPx.toDp() }
            val cellHeightDp = 300.dp
            val cellHeightPx = with(density) { cellHeightDp.toPx() }

            items.forEachIndexed { index, item ->
                if (item.assignedCell == null) {
                    val posX = (100f * index) + (100 / 2)
                    val posY = cellHeightPx + 50f
                    item.assignedCell = GridCell(
                        index = -1,
                        row = -1,
                        column = -1,
                        position = Offset(posX, posY)
                    )
                }
            }

            // Create cells
            containerLabels.forEachIndexed { index, label ->

                Box(
                    modifier = Modifier
                        .offset { IntOffset((cellWidthPx * index).toInt(), 0) }
                        .width(cellWidthDp)
                        .height(cellHeightDp)
                        .border(2.dp, Color.Black)
                        .onGloballyPositioned { coordinates ->
                            val pos = coordinates.positionInParent()
                            val size = coordinates.size
                            val cell = GridCell(
                                index = index,
                                row = 0,
                                column = index,
                                position = pos,
                                label = label
                            )
                            if (gridCells.none { it.index == index }) {
                                gridCells.add(cell)
                            } else {
                                gridCells[index] = cell
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (cellImages != null) {
                        // Render image inside the cell
                        androidx.compose.foundation.Image(
                            painter = androidx.compose.ui.res.painterResource(id = cellImages[index].imageResId),
                            contentDescription = null,
                            modifier = Modifier
                                .height(80.dp)
                                .padding(bottom = 4.dp)
                        )
                    } else {
                        // Render label text
                        Text(
                            text = label,
                            modifier = Modifier.padding(bottom = 4.dp),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Initialize items below the cells with correct spacing
            LaunchedEffect(Unit) {
                val spacingX = 100f  // Centered per item
                items.forEachIndexed { index, item ->
                    if (item.assignedCell == null) {
                        val posX = (spacingX * index) + spacingX / 2
                        val posY = cellHeightPx + 50f  // 50px below cells
                        item.assignedCell = GridCell(
                            index = -1,
                            row = -1,
                            column = -1,
                            position = Offset(posX, posY)
                        )
                    }
                }
            }

            // Render draggable items on top
            items.forEach { item ->
                ImagenArrastrable(
                    item = item,
                    gridCells = gridCells,
                    allItems = items,
                    cellWidthPx = cellWidthPx,
                    cellHeightPx = cellHeightPx,
                    initialOffset = item.assignedCell?.position ?: Offset.Zero,
                    onAssignmentChanged = { updatedAssignments ->
                        onAssignmentChanged(
                            items.map { it.assignedCell?.let { cell ->
                                "${it.id}:${cell.label ?: "Ninguno"}"
                            } ?: "${it.id}:Ninguno" }
                        )
                    }
                )
            }
        }}
}
