package com.example.tesis.vista.componente

import android.content.Context
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.Label
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimacionTexto(preguntaText)

        ImagenPregunta(
            context = context,
            idPregunta = idPregunta,
            modifier = Modifier
                .size(350.dp)
        )

        Row (){
            /**Utiliza la variable radioOptions ya que es el mismo modelo de asignacion utilizado
             * en SeleccionMultiple para poner en pantalla la cantidad de casillas de respuesta
             * que el usuario puede utilizar*/

            val isNumeric = preguntaText.lowercase().contains("cuánt") ||
                       preguntaText.lowercase().contains("núm") ||
                        preguntaText.lowercase().contains("teso")

            val chunkedOptions = radioOptions.chunked((radioOptions.size + 1) / 2)

            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                chunkedOptions.forEachIndexed { colIndex, columnItems ->

                    columnItems.forEachIndexed { rowIndex, label ->
                        val index = colIndex * ((radioOptions.size + 1) / 2) + rowIndex
                        Text(
                            text = "$label:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFF085A5D)
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .height(50.dp)
                                .width(100.dp)
                                .border(
                                    width = 3.dp, // Grosor
                                    color= Color(0xFF085A5D),
                                    shape = RoundedCornerShape(5.dp)
                                ),
                            value = respuestas.getOrNull(index) ?: "",
                            onValueChange = { newText ->
                                val filteredText = if (isNumeric) {
                                    newText.filter { it.isDigit() }
                                } else {
                                    newText
                                }
                                val nuevaRespuesta = respuestas.toMutableList()
                                if (nuevaRespuesta.size <= index) {
                                    nuevaRespuesta.addAll(List(index - nuevaRespuesta.size + 1) { "" })
                                }
                                nuevaRespuesta[index] = filteredText
                                onAnswersChanged(nuevaRespuesta)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = if (isNumeric) KeyboardType.Number else KeyboardType.Text,
                                imeAction = ImeAction.Next
                            )
                        )
                    }
                }
            }
        }
    }
}