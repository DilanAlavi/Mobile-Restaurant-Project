package com.ucb.ucbtest.meal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.ucb.domain.Meal
import com.ucb.ucbtest.categorymeal.CategoryMealComponent
import com.ucb.ucbtest.navigation.Screen
import com.ucb.ucbtest.shared.MealClickHandler
import com.ucb.ucbtest.toppick.TopPickComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealUI(
    viewModel: MealViewModel = hiltViewModel(),
    navController: NavController,
    onDetailsClick: (Meal) -> Unit = {}
) {
    // ARREGLADO: Obtener MealClickHandler aquí en el contexto @Composable correcto
    val mealClickHandler: MealClickHandler = hiltViewModel()

    val darkRed = Color(0xFFA01111)
    val red = Color(0xFFC71818)
    val lightRed = Color(0xFFBD6060)
    val lightPink = Color(0xFFF7E9E9)
    val gray = Color(0xFF8E8A8A)

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
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
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.SettingsScreen.route) }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Settings",
                            tint = Color(0xFF6B3E3E)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            when (val currentState = state) {
                is MealViewModel.MealState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = red)
                    }
                }
                is MealViewModel.MealState.Success -> {
                    val meal = currentState.meal

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        AsyncImage(
                            model = meal.strMealThumb,
                            contentDescription = meal.strMeal,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(180.dp)
                                .height(180.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Lo Más Destacado De Hoy",
                                color = red,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            Text(
                                text = "Spicy Arrabiata Penne",
                                color = Color(0xFF6B3E3E),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Text(
                                text = "Bs. 500",
                                color = red,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

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
                                onClick = { onDetailsClick(meal) },
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
                is MealViewModel.MealState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(16.dp),
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
                            .height(200.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Cargando información...")
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Top Picks",
                    color = darkRed,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )

                // ARREGLADO: TopPickComponent con MealClickHandler correcto
                TopPickComponent(
                    showTitle = false,
                    onTopPickClick = { topPick ->
                        mealClickHandler.handleTopPickClick(
                            topPick = topPick,
                            onMealFound = onDetailsClick
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Categorías",
                    color = darkRed,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) {
                    CategoryMealComponent(
                        navController = navController,
                        onCategoryClick = { categoryName -> },
                        showTitle = false,
                        columns = 2,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}