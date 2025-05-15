package com.example.tesis.vista.util

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.tesis.R

//Cargar imagen dinamicamente basado en el id de la pregunta
@Composable
fun ImagenPregunta(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    idPregunta: Int,
) {
    val imageName = "p$idPregunta"
    val imageResId = context.resources.getIdentifier(
        imageName,
        "drawable",
        context.packageName
    )
    val validImage = if (imageResId != 0) imageResId else R.drawable.monstruo_2

    Image(
        painter = painterResource(id = validImage),
        contentDescription = "Imagen para pregunta $idPregunta",
        modifier = modifier
    )
}