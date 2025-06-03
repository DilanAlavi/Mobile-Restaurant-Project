package com.ucb.ucbtest.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.ucb.ucbtest.shared.MealClickHandler
import com.ucb.ucbtest.toppick.TopPickComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onMealClick: (Meal) -> Unit = {}
) {
    val mealClickHandler: MealClickHandler = hiltViewModel()
    val red = Color(0xFFC71818)
    val darkRed = Color(0xFFA01111)
    val gray = Color(0xFF8E8A8A)

    val searchState by viewModel.searchState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra de búsqueda
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

        Button(
            onClick = { viewModel.search(searchQuery) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = red),
            enabled = searchQuery.isNotBlank()
        ) {
            Text("Buscar", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            when (val currentState = searchState) {
                is SearchViewModel.SearchState.Initial -> {
                    // Búsquedas recientes
                    if (recentSearches.isNotEmpty()) {
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
                            TextButton(onClick = viewModel::clearRecentSearches) {
                                Text("Limpiar", color = gray)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        recentSearches.forEach { search ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable { viewModel.searchFromRecent(search) },
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
                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // TopPick con precios especiales
                    TopPickComponent(
                        showTitle = true,
                        onTopPickClick = { topPick ->
                            mealClickHandler.handleTopPickClick(
                                topPick = topPick,
                                onMealFound = onMealClick
                            )
                        }
                    )
                }

                is SearchViewModel.SearchState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = red)
                    }
                }

                is SearchViewModel.SearchState.Success -> {
                    // Resultados de búsqueda
                    Text(
                        text = "Resultados (${currentState.meals.size})",
                        color = darkRed,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Grid de resultados (precio 50.0 Bs)
                    SearchResultsGrid(
                        meals = currentState.meals,
                        mealClickHandler = mealClickHandler,
                        onMealClick = onMealClick
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // TopPick al final con precios especiales
                    TopPickComponent(
                        showTitle = true,
                        onTopPickClick = { topPick ->
                            mealClickHandler.handleTopPickClick(
                                topPick = topPick,
                                onMealFound = onMealClick
                            )
                        }
                    )
                }

                is SearchViewModel.SearchState.Empty -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp)
                    ) {
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

                    Spacer(modifier = Modifier.height(24.dp))

                    TopPickComponent(
                        showTitle = true,
                        onTopPickClick = { topPick ->
                            mealClickHandler.handleTopPickClick(
                                topPick = topPick,
                                onMealFound = onMealClick
                            )
                        }
                    )
                }

                is SearchViewModel.SearchState.Error -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp)
                    ) {
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

                    Spacer(modifier = Modifier.height(24.dp))

                    TopPickComponent(
                        showTitle = true,
                        onTopPickClick = { topPick ->
                            mealClickHandler.handleTopPickClick(
                                topPick = topPick,
                                onMealFound = onMealClick
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchResultsGrid(
    meals: List<Meal>,
    mealClickHandler: MealClickHandler,
    onMealClick: (Meal) -> Unit
) {
    val rows = (meals.size + 1) / 2

    Column {
        for (rowIndex in 0 until rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val firstIndex = rowIndex * 2
                if (firstIndex < meals.size) {
                    SearchMealCard(
                        meal = meals[firstIndex],
                        onClick = {
                            mealClickHandler.handleSearchResultClick(
                                mealId = meals[firstIndex].idMeal,
                                onMealFound = onMealClick
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                val secondIndex = rowIndex * 2 + 1
                if (secondIndex < meals.size) {
                    SearchMealCard(
                        meal = meals[secondIndex],
                        onClick = {
                            mealClickHandler.handleSearchResultClick(
                                mealId = meals[secondIndex].idMeal,
                                onMealFound = onMealClick
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SearchMealCard(
    meal: Meal,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val red = Color(0xFFC71818)
    val lightPink = Color(0xFFF7E9E9)

    Card(
        colors = CardDefaults.cardColors(containerColor = lightPink),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
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
