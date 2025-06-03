// app/src/main/java/com/ucb/ucbtest/search/SearchScreen.kt
package com.ucb.ucbtest.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ucb.domain.Meal
import com.ucb.ucbtest.toppick.TopPickComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onMealClick: (Meal) -> Unit = {}
) {
    val red = Color(0xFFC71818)
    val darkRed = Color(0xFFA01111)
    val lightPink = Color(0xFFF7E9E9)
    val gray = Color(0xFF8E8A8A)

    val searchState by viewModel.searchState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra de búsqueda simplificada
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::updateSearchQuery,
            placeholder = { Text("Buscar comidas...", color = gray) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = gray
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpiar",
                            tint = gray
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = red,
                unfocusedBorderColor = gray.copy(alpha = 0.5f)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de búsqueda
        Button(
            onClick = { viewModel.search(searchQuery) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = red),
            enabled = searchQuery.isNotBlank()
        ) {
            Text("Buscar", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Contenido principal
        when (val currentState = searchState) {
            is SearchViewModel.SearchState.Initial -> {
                InitialSearchContent(
                    recentSearches = recentSearches,
                    onRecentSearchClick = viewModel::searchFromRecent,
                    onClearRecentSearches = viewModel::clearRecentSearches
                )
            }
            is SearchViewModel.SearchState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = red)
                }
            }
            is SearchViewModel.SearchState.Success -> {
                SearchResults(
                    meals = currentState.meals,
                    onMealClick = onMealClick
                )
            }
            is SearchViewModel.SearchState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No se encontraron resultados",
                            fontSize = 18.sp,
                            color = gray
                        )
                        Text(
                            text = "Intenta con otro término de búsqueda",
                            fontSize = 14.sp,
                            color = gray
                        )
                    }
                }
            }
            is SearchViewModel.SearchState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error en la búsqueda",
                            fontSize = 18.sp,
                            color = Color.Red
                        )
                        Text(
                            text = currentState.message,
                            fontSize = 14.sp,
                            color = gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InitialSearchContent(
    recentSearches: List<String>,
    onRecentSearchClick: (String) -> Unit,
    onClearRecentSearches: () -> Unit
) {
    val gray = Color(0xFF8E8A8A)
    val darkRed = Color(0xFFA01111)

    LazyColumn {
        if (recentSearches.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Búsquedas Recientes",
                        color = darkRed,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onClearRecentSearches) {
                        Text("Limpiar", color = gray)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(recentSearches) { search ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onRecentSearchClick(search) },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = search,
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        item {
            TopPickComponent(
                showTitle = true,
                onTopPickClick = { /* Handle top pick click */ }
            )
        }
    }
}

@Composable
fun SearchResults(
    meals: List<Meal>,
    onMealClick: (Meal) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(meals) { meal ->
            SearchMealCard(
                meal = meal,
                onClick = { onMealClick(meal) }
            )
        }
    }
}

@Composable
fun SearchMealCard(
    meal: Meal,
    onClick: () -> Unit
) {
    val red = Color(0xFFC71818)
    val lightPink = Color(0xFFF7E9E9)

    Card(
        colors = CardDefaults.cardColors(containerColor = lightPink),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .height(200.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            AsyncImage(
                model = meal.strMealThumb,
                contentDescription = meal.strMeal,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = meal.strMeal,
                color = Color(0xFF6B3E3E),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Bs.${meal.price.toInt()}",
                color = red,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}