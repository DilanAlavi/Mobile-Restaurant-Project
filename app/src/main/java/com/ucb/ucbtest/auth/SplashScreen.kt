package com.ucb.ucbtest.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val redColor = Color(0xFFB71C1C)

    LaunchedEffect(Unit) {
        delay(2000) // 2 segundos
        navController.navigate("login_screen") {
            popUpTo("splash_screen") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(redColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Gusto",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "total",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            )
        }

        // Botón "+" en la esquina superior derecha
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = "+",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Light
            )
        }

        // Cuadrado pequeño en la esquina inferior izquierda
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color.White.copy(alpha = 0.7f))
            )
        }
    }
}