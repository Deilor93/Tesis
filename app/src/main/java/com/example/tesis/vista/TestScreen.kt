package com.example.tesis.vista

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.tesis.R
import com.example.tesis.logica.DbClass
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TestScreen(navigationViewModel: NavigationViewModel, context: Context) {

    // Importar fuente para letras
    val mogra = FontFamily(
        Font(R.font.mogra_regular, FontWeight.Light),
        Font(R.font.mogra_regular, FontWeight.Normal),
        Font(R.font.mogra_regular, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.mogra_regular, FontWeight.Medium),
        Font(R.font.mogra_regular, FontWeight.Bold)
    )

    val db = remember { DbClass.getDatabase(context) }
    val preguntaDao = db.preguntaDao()

    var idPregunta = 1

    val pregunta = remember { mutableStateOf(preguntaDao.getTextoPreguntaById(idPregunta))}
    val cantidadPreguntas = preguntaDao.getCantidadPreguntas()

    //Contador de pregunta actual para rastrear progreso
    val currentStep = remember { mutableStateOf(0) }

    //FocusManager para cerrar el teclado en pantalla al tocar fuera
    var localFocusManager = LocalFocusManager.current

    Surface (modifier = Modifier
        .fillMaxSize(),
        color = Color(0xFFFFFF99)
    ){
        //Columna contenedora
        Column() {
            //Fila superior para mapa y Soly
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ){

                //Columna mapa
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {

                    StepsProgressBar(modifier = Modifier.fillMaxWidth(), numberOfSteps = cantidadPreguntas, currentStep = currentStep.value)
                }

                //Columna Soly
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    //GifPreview()
                    Image(
                        painter = painterResource(id = R.drawable.monstruo_2),
                        contentDescription = "Soly",
                        modifier = Modifier
                            .size(300.dp)
                    )
                }
            }
            //Fila enunciado pregunta
            Row() {
                Text(
                    text = pregunta.value,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF34D9A2),
                    fontFamily = mogra,
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .padding(12.dp)
                )
            }
            //Fila respuestas pregunta
            Row() {

            }

        }

        Row (modifier = Modifier.absoluteOffset(0.dp, 50.dp)){

            Spacer(modifier = Modifier.width(300.dp))

            Column (modifier = Modifier.align(Alignment.CenterVertically)){

                Spacer(modifier = Modifier.height(10.dp))


                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {

                        if (currentStep.value<cantidadPreguntas-1){
                            currentStep.value = currentStep.value + 1
                            idPregunta = idPregunta +1
                            pregunta.value = preguntaDao.getTextoPreguntaById(idPregunta)
                        }else if(currentStep.value<cantidadPreguntas){
                            currentStep.value = currentStep.value +1
                        }


                        Log.d("Valor pregunta", idPregunta.toString())
                        Log.d("cantidad preguntas", cantidadPreguntas.toString())
                        Log.d("Pregunta actual", currentStep.value.toString())

                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF007BFF)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center)
                        .align(Alignment.End)
                ) {
                    Text("Siguiente", color = Color.Black)
                }
            }
        }
    }
}

//Barra de progreso
@Composable
fun Step(modifier: Modifier = Modifier, isComplete: Boolean, isCurrent: Boolean) {
    val color = if (isComplete || isCurrent) Color(0xFF55DFBE) else Color.LightGray
    val innerCircleColor = if (isComplete) Color(0xFFF5B800) else Color.LightGray

    Box(modifier = modifier) {

        //Linea
        Divider(
            modifier = Modifier.align(Alignment.CenterStart),
            color = color,
            thickness = 6.dp
        )

        //Estrella
        StarCanvasSmooth(
            modifier = Modifier.size(100.dp),
            fillColor = innerCircleColor,
            borderColor = innerCircleColor
        )
    }
}

@Composable
fun StepsProgressBar(modifier: Modifier = Modifier, numberOfSteps: Int, currentStep: Int) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (step in 1..numberOfSteps) {
            Step(
                modifier = Modifier.weight(1F),
                isComplete = step <= currentStep,
                isCurrent = step == currentStep
            )
        }
    }
}

//Forma de estrella
@Composable
fun StarCanvasSmooth(
    modifier: Modifier = Modifier,
    fillColor: Color,
    borderColor: Color,
    //strokeWidth: Float = 4f,
    cornerRadius: Float = 30f,
    points: Int = 5
) {
    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val outerRadius = size.minDimension / 2.2f
        val innerRadius = outerRadius / 2.5f
        val starPath = createStarPath(center, outerRadius, innerRadius, points)

        drawIntoCanvas { canvas ->
            val paint = Paint().apply {
                this.color = fillColor
                this.style = PaintingStyle.Fill
                this.pathEffect = PathEffect.cornerPathEffect(cornerRadius)
            }
            canvas.drawPath(starPath, paint)

            val strokePaint = Paint().apply {
                this.color = borderColor
                this.style = PaintingStyle.Stroke
                //this.strokeWidth = strokeWidth
                this.pathEffect = PathEffect.cornerPathEffect(cornerRadius)
            }
            canvas.drawPath(starPath, strokePaint)
        }
    }
}

private fun createStarPath(
    center: Offset,
    outerRadius: Float,
    innerRadius: Float,
    points: Int
): Path {
    val path = Path()
    val angleStep = 360.0 / (points * 2)

    for (i in 0 until points * 2) {
        val angleDegrees = i * angleStep - 90
        val angleRadians = Math.toRadians(angleDegrees)
        val radius = if (i % 2 == 0) outerRadius else innerRadius
        val x = (center.x + radius * cos(angleRadians)).toFloat()
        val y = (center.y + radius * sin(angleRadians)).toFloat()

        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }

    path.close()
    return path
}

@Composable
fun GifLoader(resourceId: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (android.os.Build.VERSION.SDK_INT >= 28) {
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

@Composable
fun GifPreview() {

        GifLoader(
            resourceId = R.drawable.smoke_gif,
            modifier = Modifier.size(200.dp)
        )

}



