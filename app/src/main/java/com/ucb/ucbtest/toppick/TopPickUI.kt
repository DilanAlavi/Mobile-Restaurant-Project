package com.ucb.ucbtest.toppick

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ucb.domain.TopPick

/**
 * Componente reutilizable para mostrar los platos destacados (Top Picks).
 * Se puede incluir en cualquier vista existente.
 */
@Composable
fun TopPickComponent(
    viewModel: TopPickViewModel = hiltViewModel(),
    onTopPickClick: (TopPick) -> Unit = {}, // CAMBIÓ: ahora pasa TopPick completo
    showTitle: Boolean = true,
    modifier: Modifier = Modifier
) {
    val red = Color(0xFFC71818)
    val darkRed = Color(0xFFA01111)

    LaunchedEffect(Unit) {
        viewModel.getTopPicks()
    }

    val state by viewModel.state.collectAsState()

    Column(modifier = modifier) {
        if (showTitle) {
            Text(
                text = "Top Picks",
                color = darkRed,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        when (val currentState = state) {
            is TopPickViewModel.TopPickState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = red)
                }
            }
            is TopPickViewModel.TopPickState.Success -> {
                val topPicks = currentState.topPicks

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(topPicks) { topPick ->
                        TopPickCard(
                            topPick = topPick,
                            onClick = { onTopPickClick(topPick) } // CAMBIÓ: pasa TopPick completo
                        )
                    }
                }
            }
            is TopPickViewModel.TopPickState.Error -> {
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
                    Text("Cargando platos destacados...")
                }
            }
        }
    }
}

/**
 * Tarjeta para mostrar un plato destacado con su precio original,
 * precio con descuento y porcentaje de descuento.
 */
@Composable
fun TopPickCard(
    topPick: TopPick,
    onClick: () -> Unit
) {
    val red = Color(0xFFC71818)
    val gray = Color(0xFF8E8A8A)
    val lightPink = Color(0xFFF7E9E9)

    // Tarjeta principal
    Card(
        colors = CardDefaults.cardColors(containerColor = lightPink),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(180.dp)
            .height(220.dp)
            .clickable(onClick = onClick)
    ) {
        // Contenido dentro de la tarjeta
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Imagen del plato
            AsyncImage(
                model = topPick.strMealThumb,
                contentDescription = topPick.strMeal,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Nombre del plato
            Text(
                text = topPick.strMeal,
                color = Color(0xFF6B3E3E),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Precio con descuento
            Text(
                text = "Bs.${topPick.discountPrice.toInt()}",
                color = red,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Precio original y porcentaje de descuento
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Bs.${topPick.originalPrice.toInt()}",
                    color = gray,
                    fontSize = 12.sp,
                    textDecoration = TextDecoration.LineThrough
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Ahorra el ${topPick.discountPercentage}%",
                    color = gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}