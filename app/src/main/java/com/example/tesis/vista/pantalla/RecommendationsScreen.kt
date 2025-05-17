package com.example.tesis.vista.pantalla

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tesis.logica.Clasificacion
import com.example.tesis.logica.DbClass
import com.example.tesis.vista.modelo.ClasificacionConRecomendacion
import com.example.tesis.vista.util.Mogra
import com.example.tesis.vista.viewmodel.SharedViewModel

@Composable
fun RecommendationsScreen(
    navController: NavHostController,
    context: Context,
    sharedViewModel: SharedViewModel
){

    val db = remember { DbClass.getDatabase(context) }
    val clasificacionDao = db.clasificacionDao()
    val recomendacionDao = db.recomendacionDao()

    //Mapa de etiqueta con resultado de respuestas correctas
    val intelligencias = mapOf(
        "Inteligencia Lingüística" to sharedViewModel.contLing,
        "Inteligencia Lógico-Matemática" to sharedViewModel.contMat,
        "Inteligencia Espacial" to sharedViewModel.contEsp,
        "Inteligencia Musical" to sharedViewModel.contMus,
        "Inteligencia Corporal-Cinestésica" to sharedViewModel.contCor,
        "Inteligencia Interpersonal" to sharedViewModel.contInter,
        "Inteligencia Intrapersonal" to sharedViewModel.contIntra,
        "Inteligencia Naturalista" to sharedViewModel.contNatu
    )

    val clasificaciones = remember {
        mutableStateOf<List<Triple<String, Int, Clasificacion?>>>(emptyList())
    }
    val resultados = remember {
        mutableStateOf<List<ClasificacionConRecomendacion>>(emptyList())
    }

    Log.d("clasif", clasificaciones.toString())

    //Calculo para asignacion de categoria dependiendo de la cantidad de respuestas correctas
    fun calcCat(puntuacion: Int): Int = when {
        puntuacion <= 2 -> 1
        puntuacion == 3 -> 2
        else -> 3
    }

    /**Selecciona las recomendaciones a mostrar por cada inteligencia basado en el resultado
     * obtenido en cada una de ellas y las guarda en un data class con la informacion que se
     * va a mostrar en pantalla*/
    LaunchedEffect(Unit) {
        val result = intelligencias.map { (nombreInteligencia, puntuacion) ->
            val catId = calcCat(puntuacion)
            val clasificacion = clasificacionDao.getClasificacionById(catId)
            val recomendacion = clasificacion?.let {
                recomendacionDao.getRecomendacionByClasificacionYCategoria(
                    clasificacionId = it.id,
                    categoriaId = catId
                )
            }
            //Data class con la informacion que se va a mostrar en pantalla
            ClasificacionConRecomendacion(nombreInteligencia, puntuacion, clasificacion, recomendacion)
        }
        resultados.value = result
    }

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color(0xFFFFFF99)
    ){
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            //Escribe los resultados en pantalla
            resultados.value.forEach { item ->
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Mogra,
                            )
                        ){
                            append(text = "• ${item.nombre}: \n")
                        }
                        withStyle(
                            style = ParagraphStyle(
                                textIndent = TextIndent(firstLine = 35.sp)
                            )
                        ) {
                            append(text = "Puntaje ${item.puntuacion} → ${item.clasificacion?.nombre ?: "Cargando..."}")
                        }
                    },
                    modifier = Modifier.padding(vertical = 15.dp),
                    fontSize = 30.sp,
                    fontFamily = Mogra
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = ParagraphStyle(
                                textIndent = TextIndent(firstLine = 35.sp)
                            )
                        ) {
                            append(item.recomendacion?.recomendacion ?: "Recomendación no disponible")
                        }
                    },
                    style = TextStyle(fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 25.dp),
                    fontFamily = Mogra

                )
            }
        }
    }
}