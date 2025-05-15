package com.example.tesis.vista.componente

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tesis.vista.util.ImagenPregunta

//Preguntas seleccion multiple
@Composable
fun SeleccionMultiple(
    context: Context,
    idPregunta: Int,
    questionText: String,
    radioOptions: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Texto animado
        AnimacionTexto(questionText)

        Spacer(modifier = Modifier.height(16.dp))

        ImagenPregunta(
            context = context,
            idPregunta = idPregunta,
            modifier = Modifier
                .size(300.dp)
                .padding(vertical = 16.dp)
        )


        Row(
            modifier = Modifier.selectableGroup()
        ) {
            radioOptions.forEach { text ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) },
                            role = Role.RadioButton
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) }
                    )
                    Text(
                        text = text,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}