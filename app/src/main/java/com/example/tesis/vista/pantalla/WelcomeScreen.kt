package com.example.tesis.vista.pantalla

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tesis.R
import com.example.tesis.vista.util.Mogra
import com.example.tesis.vista.viewmodel.SharedViewModel

@Composable
fun WelcomeScreen(navController: NavController, sharedViewModel: SharedViewModel) {

    val estudiante = sharedViewModel.estudiante

    Row (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFF99)),
    ) {

        Column(verticalArrangement = Arrangement.Center,
            modifier = Modifier.weight(0.45f)){
            Image(
                painter = painterResource(id = R.drawable.monstruo),
                contentDescription = "Soly",
                modifier = Modifier
                    .size(900.dp)
            )
        }

        Column (modifier = Modifier.align(Alignment.CenterVertically).weight(0.45f)){
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF34D9A2),
                            fontSize = 58.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Mogra
                        )
                    ) { append("Hola ") }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFFF5500),
                            fontSize = 58.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Mogra
                        )
                    ) { append(estudiante?.nombre + "\n") }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF34D9A2),
                            fontSize = 58.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Mogra
                        )
                    ) { append("Soly quiere jugar contigo") }
                }
            )
        }
        Box(modifier = Modifier.fillMaxHeight().weight(0.1f)){
            IconButton(
                onClick = {
                    navController.navigate("test") {
                        popUpTo("intro") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Siguiente",
                    tint = Color.Black,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
    }
}