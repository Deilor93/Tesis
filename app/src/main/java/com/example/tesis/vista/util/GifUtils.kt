package com.example.tesis.vista.util

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.tesis.R

//Loader para poder mostrar GIFs en pantalla
@Composable
fun GifLoader(resourceId: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    AsyncImage(
        model = resourceId,
        contentDescription = "GIF animado",
        modifier = modifier,
        imageLoader = imageLoader
    )
}
//Carga el gif utilizando el loader creado
@Composable
fun MostrarGif(modifier: Modifier) {
    GifLoader(
        resourceId = R.drawable.smoke_gif,
        modifier = modifier
    )
}