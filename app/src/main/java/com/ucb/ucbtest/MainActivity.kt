package com.ucb.ucbtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.ucb.ucbtest.auth.AuthNavigation
import com.ucb.ucbtest.bottomNav.BottomBar
import com.ucb.ucbtest.navigation.AppNavigation
import com.ucb.ucbtest.auth.AuthState
import com.ucb.ucbtest.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Ucbtest)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainApp()
        }
    }
}

@Composable
fun MainApp(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val authNavController = rememberNavController()
    val mainNavController = rememberNavController()

    // ✅ Manejar cambios de estado de autenticación
    LaunchedEffect(authState) {
        val currentState = authState
        println("🏠 MainActivity: Estado cambió a: $currentState")

        when (currentState) {
            is AuthState.Authenticated -> {
                println("🎉 MainActivity: ¡USUARIO AUTENTICADO! ${currentState.user.name}")
            }
            is AuthState.Unauthenticated -> {
                println("🚪 MainActivity: Usuario desautenticado - Limpiando navegación")
                // Limpiar el stack de navegación principal y redirigir a splash
                authNavController.navigate("splash_screen") {
                    // Limpiar todo el stack de navegación
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            else -> println("🏠 MainActivity: Estado: $currentState")
        }
    }

    // ✅ LOG DE VERIFICACIÓN DE INSTANCIA
    println("🔍 MainActivity: AuthViewModel instance: ${authViewModel.hashCode()}")

    when (authState) {
        is AuthState.Loading,
        is AuthState.Unauthenticated,
        is AuthState.Error -> {
            println("🔄 MainActivity: Mostrando AuthNavigation")
            AuthNavigation(
                navController = authNavController,
                onAuthSuccess = {
                    // Opcional: Lógica adicional cuando se autentica exitosamente
                    println("✅ MainActivity: Login exitoso")
                },
                authViewModel = authViewModel // ✅ Pasar la misma instancia
            )
        }

        is AuthState.Authenticated -> {
            println("🎯 MainActivity: ¡MOSTRANDO APP PRINCIPAL!")
            Scaffold(
                bottomBar = { BottomBar(mainNavController) }
            ) { innerPadding ->
                AppNavigation(
                    navController = mainNavController,
                    innerPadding = innerPadding
                )
            }
        }
    }
}