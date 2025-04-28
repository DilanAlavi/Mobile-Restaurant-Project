package com.ucb.ucbtest.categorymeal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ucb.domain.CategoryMeal

/**
 * Componente reutilizable para mostrar una cuadrícula de categorías de comida.
 * Se puede incluir en cualquier vista existente.
 */
@Composable
fun CategoryMealComponent(
    viewModel: CategoryMealViewModel = hiltViewModel(),
    onCategoryClick: (String) -> Unit = {},
    showTitle: Boolean = true,
    columns: Int = 2,
    modifier: Modifier = Modifier
) {
    val red = Color(0xFFC71818)
    val darkRed = Color(0xFFA01111)

    LaunchedEffect(Unit) {
        viewModel.getCategories()
    }

    val state by viewModel.state.collectAsState()

    Column(modifier = modifier) {
        if (showTitle) {
            Text(
                text = "Categorías",
                color = darkRed,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        when (val currentState = state) {
            is CategoryMealViewModel.CategoryMealState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = red)
                }
            }
            is CategoryMealViewModel.CategoryMealState.Success -> {
                val categories = currentState.categories

                // IMPORTANTE: No hay Modifier.fillMaxSize() aquí para evitar restricciones de altura infinita
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    // No usar fillMaxWidth() aquí
                    modifier = Modifier.heightIn(max = 800.dp) // Limitar altura máxima
                ) {
                    items(categories) { category ->
                        CategoryCardSimple(
                            category = category,
                            onClick = { onCategoryClick(category.strCategory) }
                        )
                    }
                }
            }
            is CategoryMealViewModel.CategoryMealState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: ${currentState.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Cargando categorías...")
                }
            }
        }
    }
}

/**
 * Tarjeta de categoría con el título dentro del borde de la tarjeta,
 * debajo de la imagen.
 */
@Composable
fun CategoryCardSimple(
    category: CategoryMeal,
    onClick: () -> Unit
) {
    val red = Color(0xFFC71818)

    // Tarjeta principal que contiene tanto la imagen como el texto
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .size(120.dp)
            .clickable(onClick = onClick)
    ) {
        // Contenido dentro de la tarjeta
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagen centrada arriba
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    model = category.strCategoryThumb,
                    contentDescription = category.strCategory,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(2.dp)
                )
            }

            // Texto en la parte inferior dentro de la tarjeta
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxWidth()
            ) {
                Text(
                    text = category.strCategory.uppercase(),
                    color = red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}