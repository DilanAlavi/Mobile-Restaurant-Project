package com.ucb.ucbtest.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import android.content.Intent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel(),
    onItemSelected: (String) -> Unit = {}
) {
    val settingsItems by viewModel.settingsItems.collectAsStateWithLifecycle()
    val otherItems by viewModel.otherItems.collectAsStateWithLifecycle()
    val isLoggingOut by viewModel.isLoggingOut.collectAsStateWithLifecycle()
    val shouldRestartApp by viewModel.shouldRestartApp.collectAsStateWithLifecycle()

    val context = LocalContext.current

    // ✅ Manejar reinicio de app después del logout
    LaunchedEffect(shouldRestartApp) {
        if (shouldRestartApp) {
            println("🔄 SettingsScreen: Reiniciando app después del logout...")

            // Reiniciar la Activity principal
            val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

            // Terminar la actividad actual
            if (context is android.app.Activity) {
                context.finish()
            }

            viewModel.onRestartHandled()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuraciones") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Primera sección - Items principales
            items(settingsItems) { item ->
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable {
                            when (item) {
                                "Mis pedidos" -> navController.navigate("orders_history")
                                else -> onItemSelected(item)
                            }
                        }
                )
                HorizontalDivider()
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Segunda sección - Otros items
            items(otherItems) { item ->
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onItemSelected(item) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            // ✅ Sección de logout
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isLoggingOut) {
                            println("🔘 SettingsScreen: Botón logout presionado")
                            viewModel.signOut()
                        }
                        .padding(vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(

                        contentDescription = "Cerrar sesión",
                        imageVector = Icons.Default.ExitToApp,
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )

                    if (isLoggingOut) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(
                        text = if (isLoggingOut) "Cerrando sesión..." else "Cerrar sesión",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = if (isLoggingOut) Color.Gray else Color.Red
                        )
                    )
                }
            }
        }

    }
}
