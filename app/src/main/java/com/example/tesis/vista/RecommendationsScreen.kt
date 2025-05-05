package com.example.tesis.vista

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tesis.R
import com.example.tesis.logica.Clasificacion
import com.example.tesis.logica.DbClass
import com.example.tesis.logica.Recomendacion

@Composable
fun RecommendationsScreen(
    navController: NavHostController,
    context: Context,
    sharedViewModel: SharedViewModel
){

    val mogra = FontFamily(
        Font(R.font.mogra_regular, FontWeight.Light),
        Font(R.font.mogra_regular, FontWeight.Normal),
        Font(R.font.mogra_regular, FontWeight.Normal, FontStyle.Italic),
        Font(R.font.mogra_regular, FontWeight.Medium),
        Font(R.font.mogra_regular, FontWeight.Bold)
    )

    val db = remember { DbClass.getDatabase(context) }
    val clasificacionDao = db.clasificacionDao()
    val recomendacionDao = db.recomendacionDao()


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

    data class ClasificacionConRecomendacion(
        val nombre: String,
        val puntuacion: Int,
        val clasificacion: Clasificacion?,
        val recomendacion: Recomendacion?
    )

    val clasificaciones = remember {
        mutableStateOf<List<Triple<String, Int, Clasificacion?>>>(emptyList())
    }
    val resultados = remember {
        mutableStateOf<List<ClasificacionConRecomendacion>>(emptyList())
    }

    Log.d("clasif", clasificaciones.toString())

    fun calcCat(puntuacion: Int): Int = when {
        puntuacion <= 2 -> 1
        puntuacion == 3 -> 2
        else -> 3
    }

    LaunchedEffect(Unit) {
        val result = intelligencias.map { (name, puntuacion) ->
            val catId = calcCat(puntuacion)
            val clasificacion = clasificacionDao.getClasificacionById(catId)
            val recomendacion = clasificacion?.let {
                recomendacionDao.getRecomendacionByClasificacionYCategoria(
                    clasificacionId = it.id,
                    categoriaId = catId
                )
            }
            ClasificacionConRecomendacion(name, puntuacion, clasificacion, recomendacion)
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
            resultados.value.forEach { item ->
                Text(
                    text = "${item.nombre}: Puntaje ${item.puntuacion} → ${item.clasificacion?.nombre ?: "Cargando..."}",
                    modifier = Modifier.padding(vertical = 8.dp),
                    fontSize = 35.sp,
                    fontFamily = mogra
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = ParagraphStyle(
                                textIndent = TextIndent(firstLine = 20.sp)
                            )
                        ) {
                            append(item.recomendacion?.recomendacion ?: "Recomendación no disponible")
                        }
                    },
                    style = TextStyle(fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 16.dp),
                    fontFamily = mogra

                )
            }
        }

    }
}