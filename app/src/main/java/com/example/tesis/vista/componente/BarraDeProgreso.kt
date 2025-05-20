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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalDensity
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

    val scrollbarHeight = 4.dp

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
        // Main steps row
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 0 until numberOfSteps) {

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
            // Optional: custom scrollbar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(scrollbarHeight)
                    .padding(horizontal = 8.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val trackColor = Color.LightGray.copy(alpha = 0.3f)
                    val thumbColor = Color.DarkGray

                    val totalContentWidth = numberOfSteps * stepWidthPx
                    val visibleWidthCanvas = size.width

                    if (totalContentWidth > visibleWidthCanvas) {
                        drawRoundRect(
                            color = trackColor,
                            size = size,
                            cornerRadius = CornerRadius(10f)
                        )

                        val thumbWidth = visibleWidthCanvas * (visibleWidthCanvas / totalContentWidth)
                        val thumbOffset = scrollState.value * (visibleWidthCanvas / totalContentWidth)

                        drawRoundRect(
                            color = thumbColor,
                            topLeft = Offset(thumbOffset, 0f),
                            size = Size(thumbWidth, size.height),
                            cornerRadius = CornerRadius(10f)
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

    // Animate when the step becomes complete
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
        // Draw animated connecting line
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

        if (stepIndex == 0){
            Step(
                modifier = modifier,
                isComplete = isComplete,
                innerCircleColor = fillColor,
            )
        }else{
            Step(
                modifier = modifier,
                isComplete = showStarComplete,
                innerCircleColor = fillColor,
            )
        }
    }
}

//Crea los pasos de la barra de progreso para poder mostrar en pantalla
@Composable
fun Step(modifier: Modifier = Modifier, isComplete: Boolean, innerCircleColor: Color) {
    //val color = if (isComplete || isCurrent) Color(0xFF55DFBE) else Color.LightGray
    //val innerCircleColor = if (isComplete) Color(0xFFF5B800) else Color.LightGray
    Box(modifier = modifier) {
        /*Divider(
            modifier = Modifier.align(Alignment.CenterStart),
            color = color,
            thickness = 6.dp
        )*/
        if(isComplete){
            StarCanvasSmooth(
                modifier = modifier,
                fillColor = innerCircleColor,
                borderColor = innerCircleColor
            )
        } else{
            StarCanvasSmooth(
                modifier = modifier,
                fillColor = Color.LightGray,
                borderColor = Color.LightGray
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
                this.pathEffect = PathEffect.cornerPathEffect(cornerRadius)
            }
            canvas.drawPath(starPath, strokePaint)
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
