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
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tesis.vista.util.ImagenPregunta

//Pregunta patron
@Composable
fun PreguntaPatronInteractivo(
    context: Context,
    idPregunta: Int,
    preguntaText: String,
    currentPattern: String,
    onPatternChanged: (String) -> Unit,
    rows: Int = 4,
    columns: Int = 4,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AnimacionTexto(preguntaText)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            ImagenPregunta(
                context = context,
                idPregunta = idPregunta,
                modifier = Modifier
                    .size(350.dp)
                    .padding(vertical = 15.dp)
            )

            // Grid del patron para seleccionar
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                    .fillMaxHeight(0.8f)
            ) {
                FlexiblePatternGrid(
                    rows = rows,
                    columns = columns,
                    initialPattern = currentPattern,
                    onPatternCompleted = onPatternChanged,
                    dotRadius = 20f, // smaller dots
                )
            }
        }
        Button(
            onClick = { onPatternChanged("") },
            modifier = Modifier
                .padding(top = 15.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Borrar patrón")
        }
    }
}