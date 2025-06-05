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

    when (authState) {
        is AuthState.Loading,
        is AuthState.Unauthenticated,
        is AuthState.Error -> {
            AuthNavigation(
                navController = authNavController,
                onAuthSuccess = { /* No usado */ }
            )
        }

        is AuthState.Authenticated -> {
            Scaffold(
                bottomBar = { BottomBar(mainNavController) }
            ) { innerPadding ->
                AppNavigation(mainNavController, innerPadding)
            }
        }
    }
}