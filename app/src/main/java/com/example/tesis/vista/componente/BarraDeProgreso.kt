package com.example.tesis.vista.componente

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

//Crea la barra de progreso en pantalla
@Composable
fun StepsProgressBar(
    modifier: Modifier = Modifier,
    numberOfSteps: Int,
    currentStep: Int
) {
    val scrollState = rememberScrollState()
    val stepWidth = 140.dp
    val density = LocalDensity.current
    val stepWidthPx = with(density) { stepWidth.toPx() }

    BoxWithConstraints(
        modifier = modifier.height(100.dp)
    ) {
        val visibleWidth = this@BoxWithConstraints.maxWidth
        val visibleWidthPx = with(density) { visibleWidth.toPx() }

        // Mueve el scroll automaticamente para mantener el siguiente paso siempre visible
        LaunchedEffect(currentStep) {
            if (currentStep >= 4) {
                val nextStepRightEdge = ((currentStep + 1) * stepWidthPx)
                val scrollTo = (nextStepRightEdge - visibleWidthPx).coerceAtLeast(0f)
                scrollState.animateScrollTo(
                    scrollTo.toInt(),
                    animationSpec = tween(durationMillis = 5000)
                )
            }
        }

        Column{
        // Fila para mostrar los pasos de forma lineal lado a lado
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 8.dp)
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until numberOfSteps) {
                    //Validacion para que las estrellas multiplos de 5 sean mas grandes que las demas
                    if ((i - 4) % 5 == 0 && i >= 4){
                        StepWithLine(
                            modifier = Modifier.size(120.dp),
                            stepIndex = i,
                            isComplete = i < currentStep,
                            fillColor = Color(0xFFF5B800)
                        )
                    }else{
                        StepWithLine(
                            modifier = Modifier.size(70.dp),
                            stepIndex = i,
                            isComplete = i < currentStep,
                            fillColor = Color(0xFFF5B800)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StepWithLine(
    modifier: Modifier,
    stepIndex: Int,
    isComplete: Boolean,
    fillColor: Color
) {
    val animatedProgress = remember { Animatable(0f) }

    // Anima el llenado de la barra al completar un paso
    LaunchedEffect(isComplete) {
        if (isComplete) {
            animatedProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000)
            )
        } else {
            animatedProgress.snapTo(0f)
        }
    }

    val showStarComplete = animatedProgress.value >= 0.95f

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Dibuja la linea de conexion para animar
        if (stepIndex > 0) {
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(40.dp)
            ) {
                // Linea gris de fondo, siempre completa hasta el final de la barra
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.LightGray)
                )

                // Overlay animado pintado sobre la linea gris para mostrar progreso
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = animatedProgress.value)
                        .background(Color(0xFF55DFBE))
                )
            }
        }
        //Validacion para que el delay de pintado de la estella se aplique solo a partir de la segunda
        if (stepIndex == 0){
            Step(
                modifier = modifier,
                isComplete = isComplete,
                innerCircleColor = fillColor,
                number = stepIndex
            )
        }else{
            Step(
                modifier = modifier,
                isComplete = showStarComplete,
                innerCircleColor = fillColor,
                number = stepIndex
            )
        }
    }
}

//Crea los pasos de la barra de progreso para poder mostrar en pantalla
@Composable
fun Step(modifier: Modifier = Modifier, isComplete: Boolean, innerCircleColor: Color, number: Int) {

    Box(modifier = modifier) {
        if(isComplete){
            StarCanvasSmooth(
                modifier = modifier,
                fillColor = innerCircleColor,
                borderColor = Color(0xFFC29102),
                number = number + 1
            )
        } else{
            StarCanvasSmooth(
                modifier = modifier,
                fillColor = Color.LightGray,
                borderColor = Color(0xFFC29102),
                number = number + 1
            )
        }
    }
}

@Composable
fun StarRating(
    rating: Int, // Numero de estrellas llenas (de 0 a 5)
    modifier: Modifier = Modifier,
    starColor: Color = Color(0xFFF5B800),
    emptyStarColor: Color = Color.LightGray,
    starSize: Dp = 40.dp
) {
    Row(modifier = modifier) {
        for (i in 1..5) {
            val color = if (i <= rating) starColor else emptyStarColor
            val borderColor = if (i <= rating) Color(0xFFC29102) else emptyStarColor
            StarCanvasSmooth(
                modifier = Modifier.size(starSize).padding(end = 4.dp),
                fillColor = color,
                borderColor = borderColor
            )
        }
    }
}

//Dibuja la forma de estrella creada
@Composable
fun StarCanvasSmooth(
    modifier: Modifier = Modifier,
    fillColor: Color,
    borderColor: Color,
    number: Int? = null,
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
                this.strokeWidth = 5f
                this.pathEffect = PathEffect.cornerPathEffect(cornerRadius)
            }
            canvas.drawPath(starPath, strokePaint)
            number?.let {
                val textPaint = android.graphics.Paint().apply {
                    color = borderColor.toArgb()
                    textAlign = android.graphics.Paint.Align.CENTER
                    textSize = size.minDimension / 3f
                    isAntiAlias = true
                    typeface = android.graphics.Typeface.DEFAULT_BOLD
                }
                canvas.nativeCanvas.drawText(
                    it.toString(),
                    center.x,
                    center.y + textPaint.textSize / 3f, // center vertically
                    textPaint
                )
            }
        }
    }
}

// Crea el patron de estrella a dibujar
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