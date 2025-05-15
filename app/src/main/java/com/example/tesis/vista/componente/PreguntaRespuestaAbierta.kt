package com.example.tesis.vista.componente

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tesis.vista.util.ImagenPregunta

//Pregunta abierta
@Composable
fun PreguntaRespuestaAbierta(
    context: Context,
    idPregunta: Int,
    preguntaText: String,
    radioOptions: List<String>,
    respuestas: List<String>,
    onAnswersChanged: (List<String>) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimacionTexto(preguntaText)

        Spacer(modifier = Modifier.height(16.dp))

        ImagenPregunta(
            idPregunta = idPregunta,
            modifier = Modifier
                .size(300.dp)
                .padding(vertical = 16.dp)
        )


        Row {
            radioOptions.forEachIndexed { index, label ->
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .widthIn(max = 5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    OutlinedTextField(
                        value = respuestas.getOrNull(index) ?: "",
                        onValueChange = { newText ->
                            val nuevaRespuesta = respuestas.toMutableList()
                            if (nuevaRespuesta.size <= index) {
                                nuevaRespuesta.addAll(List(index - nuevaRespuesta.size + 1) { "" })
                            }
                            nuevaRespuesta[index] = newText
                            onAnswersChanged(nuevaRespuesta)
                        },
                        //label = { Text(label.ifBlank { "Respuesta ${index + 1}" }) },
                        singleLine = true,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .height(100.dp)
                    )
                }
            }
        }
    }
}