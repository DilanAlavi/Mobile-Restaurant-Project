package com.ucb.ucbtest

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
    val context = LocalContext.current
    val isConnected = remember { mutableStateOf(isInternetAvailable(context)) }

    if (!isConnected.value) {
        NoInternetScreen(onRetry = {
            isConnected.value = isInternetAvailable(context)
        })
        return
    }

    val authState by authViewModel.authState.collectAsStateWithLifecycle()
    val authNavController = rememberNavController()
    val mainNavController = rememberNavController()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                println("🎉 Usuario autenticado")
            }
            is AuthState.Unauthenticated -> {
                authNavController.navigate("splash_screen") {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            }
            else -> {}
        }
    }

    when (authState) {
        is AuthState.Loading,
        is AuthState.Unauthenticated,
        is AuthState.Error -> {
            AuthNavigation(
                navController = authNavController,
                onAuthSuccess = {
                    println("✅ Login exitoso")
                },
                authViewModel = authViewModel
            )
        }

        is AuthState.Authenticated -> {
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

@Composable
fun NoInternetScreen(onRetry: () -> Unit) {
    val brown = Color(0xFF8B4513)

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Sin conexión a Internet")
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = brown)
            ) {
                Text(text = "Reintentar", color = Color.White)
            }
        }
    }
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}
