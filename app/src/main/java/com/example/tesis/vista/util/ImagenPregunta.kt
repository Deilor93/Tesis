package com.example.tesis.vista.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.tesis.R

//Cargar imagen dinamicamente basado en el id de la pregunta
@Composable
fun ImagenPregunta(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    idPregunta: Int,
) {
    val imageName = "p$idPregunta"
    //Se utiliza getIdentifier para poder cargar dinamicamente la imagen a mostrar
    val imageResId = context.resources.getIdentifier(
        imageName,
        "drawable",
        context.packageName
    )
    val validImage = if (imageResId != 0) imageResId else R.drawable.monstruo

    MostrarGif(
        modifier = modifier,
        resourceId = validImage
    )
}