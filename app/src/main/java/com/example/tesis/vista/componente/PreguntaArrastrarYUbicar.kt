package com.example.tesis.vista.component

import ImagenArrastrable
import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.tesis.R
import com.example.tesis.vista.componente.AnimacionTexto
import com.example.tesis.vista.modelo.DraggableItem
import com.example.tesis.vista.modelo.GridCell

@Composable
fun PreguntaArrastrarYUbicar(
    context: Context,
    idPregunta: Int,
    preguntaText: String,
    items: List<DraggableItem>,
    gridSize: Int = 4,
    onPositionsSaved: (String) -> Unit
) {
    val gridCells = remember { mutableStateListOf<GridCell>() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Question text
        AnimacionTexto(preguntaText)

        Spacer(modifier = Modifier.height(16.dp))

        // Row with image and grid inside the same container
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Reference image based on questionId
            val imageName = "p$idPregunta"
            val imageResId = context.resources.getIdentifier(
                imageName, "drawable", context.packageName
            )
            val validImage = if (imageResId != 0) imageResId else R.drawable.monstruo_2

            Image(
                painter = painterResource(id = validImage),
                contentDescription = "Referencia $idPregunta",
                modifier = Modifier
                    .size(300.dp)
                    .padding(8.dp)
            )

            // Grid and objects sharing same Box
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .padding(8.dp)
            ) {
                var cellSize by remember { mutableStateOf(0f) }

                // Draw the grid
                Canvas(modifier = Modifier.fillMaxSize()) {
                    cellSize = size.width / gridSize
                    gridCells.clear()
                    for (row in 0 until gridSize) {
                        for (col in 0 until gridSize) {
                            val x = col * cellSize + cellSize / 2
                            val y = row * cellSize + cellSize / 2
                            gridCells.add(GridCell(row, col, Offset(x, y)))
                            drawRect(
                                color = Color.Black,
                                topLeft = Offset(col * cellSize, row * cellSize),
                                size = androidx.compose.ui.geometry.Size(cellSize, cellSize),
                                style = Stroke(width = 2f)
                            )
                        }
                    }
                }

                // Draggable objects start below the grid inside the same box
                items.forEachIndexed { index, item ->
                    ImagenArrastrable(
                        item = item,
                        gridCells = gridCells,
                        initialOffset = Offset(
                            x = 20f + index * 90f,
                            y = 320f // visually below grid inside same Box
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val resultString = items.joinToString(",") { item ->
                val assigned = item.assignedCell
                "${item.id}:${assigned?.row}-${assigned?.column}"
            }
            onPositionsSaved(resultString)
        }) {
            Text("Guardar posiciones")
        }
    }
}


