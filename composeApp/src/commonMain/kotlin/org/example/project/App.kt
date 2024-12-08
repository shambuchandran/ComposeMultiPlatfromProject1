package org.example.project

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.PlatformContext
import org.example.project.ui.ForecastScreen
import org.example.project.ui.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(navController, startDestination = "home"){
            composable("home"){
                HomeScreen(navController)
            }
            composable("forecast"){
                ForecastScreen(navController)
            }
        }
    }
}