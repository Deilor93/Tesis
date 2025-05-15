package com.example.tesis.vista.pantalla

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tesis.vista.viewmodel.SharedViewModel

class MainScreen : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModelStoreOwner = this
            AppNavigation(context = this, viewModelStoreOwner = viewModelStoreOwner)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(context: Context, viewModelStoreOwner: MainScreen) {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = viewModel(viewModelStoreOwner)

    NavHost(
        navController = navController,
        startDestination = "intro"
    ) {
        composable("intro") {
            IntroScreen(navController = navController, context = context, sharedViewModel)
        }

        composable("welcome") {
            WelcomeScreen(navController = navController, sharedViewModel)
        }
        composable("test"){
            TestScreen(navController = navController, context = context, sharedViewModel)
        }
        composable("results"){
            ResultsScreen(navController = navController, context = context, sharedViewModel)
        }
        composable("recommendations"){
            RecommendationsScreen(navController = navController, context = context, sharedViewModel)
        }
    }
}

