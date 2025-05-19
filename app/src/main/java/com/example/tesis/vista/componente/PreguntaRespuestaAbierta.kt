package com.example.tesis.vista.componente

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimacionTexto(preguntaText)

        //Spacer(modifier = Modifier.height(16.dp))



        Row (
            modifier = Modifier
                .padding(start = 100.dp)

        ){
            /**Utiliza la variable radioOptions ya que es el mismo modelo de asignacion utilizado
             * en SeleccionMultiple para poner en pantalla la cantidad de casillas de respuesta
             * que el usuario puede utilizar*/

            val isNumeric = preguntaText.lowercase().contains("cuánt") ||
                       preguntaText.lowercase().contains("núm")

            val chunkedOptions = radioOptions.chunked((radioOptions.size + 1) / 2)

            Row(
                modifier = Modifier
                    .padding(start = 100.dp)
            ) {
                ImagenPregunta(
                    context = context,
                    idPregunta = idPregunta,
                    modifier = Modifier
                        .size(350.dp)
                )

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, top = 100.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    chunkedOptions.forEachIndexed { colIndex, columnItems ->
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            columnItems.forEachIndexed { rowIndex, label ->
                                val index = colIndex * ((radioOptions.size + 1) / 2) + rowIndex

                                OutlinedTextField(
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
                                    label = { Text(text = label) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = if (isNumeric) KeyboardType.Number else KeyboardType.Text,
                                        imeAction = ImeAction.Next
                                    ),
                                    //modifier = Modifier
                                    //    .padding(top = 100.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}