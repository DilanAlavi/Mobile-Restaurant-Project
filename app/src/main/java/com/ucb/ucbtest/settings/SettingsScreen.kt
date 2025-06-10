package com.ucb.ucbtest.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController, // Recibe el NavController para navegar
    viewModel: SettingsViewModel = hiltViewModel(),
    onItemSelected: (String) -> Unit = {}
) {
    val settingsItems by viewModel.settingsItems.collectAsState()
    val otherItems by viewModel.otherItems.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuraciones") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Regresa a la pantalla anterior
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
                Divider()
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

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
        }

    }
}
