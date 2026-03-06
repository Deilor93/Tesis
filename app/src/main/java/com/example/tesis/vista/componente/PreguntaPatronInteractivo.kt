package com.example.tesis.vista.componente

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tesis.vista.util.ImagenPregunta

//Pregunta patron
@Composable
fun PreguntaPatronInteractivo(
    modifier: Modifier = Modifier,
    context: Context,
    idPregunta: Int,
    preguntaText: String,
    currentPattern: String,
    onPatternChanged: (String) -> Unit,
    rows: Int = 4,
    columns: Int = 4
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimacionTexto(preguntaText)

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 350.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            ImagenPregunta(
                context = context,
                idPregunta = idPregunta,
                modifier = Modifier
                    .size(350.dp)
                    .weight(0.5f)
            )

            // Grid del patron para seleccionar
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                FlexiblePatternGrid(
                    rows = rows,
                    columns = columns,
                    initialPattern = currentPattern,
                    onPatternCompleted = onPatternChanged,
                    dotRadius = 20f,
                )
            }
        }
        ElevatedButton(
            onClick = { onPatternChanged("") },
            modifier = Modifier
                .padding(top = 15.dp)
                .align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF34D9A2)),
            shape = RoundedCornerShape(8.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 20.dp)
        ) {
            Text("Borrar patrón")
        }
    }
}