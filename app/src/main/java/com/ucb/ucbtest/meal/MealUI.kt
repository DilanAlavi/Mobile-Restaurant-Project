package com.ucb.ucbtest.meal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ucb.ucbtest.categorymeal.CategoryMealComponent
import com.ucb.ucbtest.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealUI(
    viewModel: MealViewModel = hiltViewModel(),
    navController: NavController,
    onDetailsClick: () -> Unit = {}
) {
    // Definición de colores basados en la imagen compartida
    val darkRed = Color(0xFFA01111)
    val red = Color(0xFFC71818)
    val lightRed = Color(0xFFBD6060)
    val lightPink = Color(0xFFF7E9E9)
    val gray = Color(0xFF8E8A8A)

    LaunchedEffect(Unit) {
        // Al cargar la pantalla, buscar "Arrabiata" como mencionaste
        viewModel.getMeal("Arrabiata")
    }

    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Gusto ",
                            color = Color(0xFF6B3E3E),
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                        Text(
                            text = "total",
                            color = Color(0xFF6B3E3E),
                            fontWeight = FontWeight.Normal,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            fontSize = 24.sp
                        )
                    }
                },
                navigationIcon = { // 👈 Aquí agregamos el botón hamburguesa
                    IconButton(onClick = { navController.navigate(Screen.SettingsScreen.route) }) {  // Navegar a SettingsScreen
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Settings",
                            tint = Color(0xFF6B3E3E) // color del icono
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = { /* Book seat action */ },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = lightPink,
                            contentColor = Color(0xFF6B3E3E)
                        ),
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text("Book seat")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { padding ->
        // Usamos una Column simple sin scroll
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Sección de plato destacado - similar a como lo tenías
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                when (val currentState = state) {
                    is MealViewModel.MealState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = red)
                        }
                    }
                    is MealViewModel.MealState.Success -> {
                        val meal = currentState.meal

                        // Destacado del día
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "Lo Más Destacado De Hoy",
                                color = red,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            Text(
                                text = meal.strMeal,
                                color = Color(0xFF6B3E3E),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Imagen del plato
                                AsyncImage(
                                    model = meal.strMealThumb,
                                    contentDescription = meal.strMeal,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(150.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                // Información de precio
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Bs. ${meal.price.toInt()}",
                                            color = red,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Bs. 1000",
                                            color = gray,
                                            fontSize = 14.sp,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Ahorra el 50%",
                                            color = gray,
                                            fontSize = 14.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Button(
                                        onClick = onDetailsClick,
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = red,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text("Ver Detalles")
                                    }
                                }
                            }
                        }
                    }
                    is MealViewModel.MealState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Error: ${currentState.message}",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    else -> {
                        // Estado inicial
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Cargando información...")
                        }
                    }
                }
            }

            // Sección de categorías con título
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Categorías",
                    color = darkRed,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }

            // Componente de categorías con altura fija para evitar problemas de scroll anidado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp) // Altura fija para el grid
            ) {
                CategoryMealComponent(
                    onCategoryClick = { categoryName ->
                        // Acción al hacer clic en una categoría
                    },
                    showTitle = false, // No mostramos el título del componente
                    columns = 2,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}