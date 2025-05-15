package com.example.tesis.vista.componente

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun PreguntaPatron(
    context: Context,
    idPregunta: Int,
    preguntaText: String,
    respuestaPatron: String,
    onPatternChanged: (String) -> Unit,
    gridSize: Int = 3
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimacionTexto(preguntaText)

        Spacer(modifier = Modifier.height(16.dp))

        // Image and Pattern side by side
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ImagenPregunta(
                context = context,
                idPregunta = idPregunta,
                modifier = Modifier
                    .size(250.dp)
                    .padding(8.dp)
            )

            FlexiblePatternGrid(
                gridSize = gridSize,
                initialPattern = respuestaPatron,
                onPatternCompleted = onPatternChanged,
                modifier = Modifier
                    .size(250.dp) // fixed size matching image
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { onPatternChanged("") }) {
            Text("Limpiar patrón")
        }
    }
}