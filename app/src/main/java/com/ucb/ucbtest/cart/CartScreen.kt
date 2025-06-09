package com.ucb.ucbtest.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ucb.domain.CartItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel()
) {
    val red = Color(0xFFC71818)
    val darkRed = Color(0xFFA01111)
    val lightPink = Color(0xFFF7E9E9)
    val gray = Color(0xFF8E8A8A)

    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Carrito",
                        color = darkRed,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, // ARREGLADO: Versión no deprecada
                            contentDescription = "Volver",
                            tint = darkRed
                        )
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
        ) {
            if (state.isEmpty) {
                // Carrito vacío
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Tu carrito está vacío",
                            fontSize = 20.sp,
                            color = gray,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Agrega algunos platos deliciosos",
                            fontSize = 16.sp,
                            color = gray
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(containerColor = red)
                        ) {
                            Text("Explorar Comidas", color = Color.White)
                        }
                    }
                }
            } else {
                // Carrito con items
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.items) { cartItem ->
                        CartItemCard(
                            cartItem = cartItem,
                            onQuantityChange = { quantity ->
                                viewModel.updateQuantity(cartItem.meal.idMeal, quantity)
                            },
                            onRemove = {
                                viewModel.removeFromCart(cartItem.meal.idMeal)
                            }
                        )
                    }
                }

                // Total y botón de pagar
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = lightPink),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total (${state.itemCount} items):",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = darkRed
                            )
                            Text(
                                text = "Bs. ${state.total.toInt()}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = red
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { /* TODO: Implementar orden */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = red),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Button(
                                onClick = { navController.navigate("checkout") }, // CAMBIAR esta línea
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = red),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    "Ordenar Todo",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit
) {
    val red = Color(0xFFC71818)
    val gray = Color(0xFF8E8A8A)

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen
            AsyncImage(
                model = cartItem.meal.strMealThumb,
                contentDescription = cartItem.meal.strMeal,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Info del plato
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.meal.strMeal,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333),
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Bs. ${cartItem.meal.price.toInt()} c/u",
                    fontSize = 14.sp,
                    color = gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Controles de cantidad
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón menos (usando Text en lugar de Remove)
                    IconButton(
                        onClick = { onQuantityChange(cartItem.quantity - 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Text(
                            text = "−", // ARREGLADO: Usar texto en lugar de icono Remove
                            color = red,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Cantidad
                    Text(
                        text = cartItem.quantity.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp),
                        textAlign = TextAlign.Center
                    )

                    // Botón más
                    IconButton(
                        onClick = { onQuantityChange(cartItem.quantity + 1) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Aumentar",
                            tint = red,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Precio total y botón eliminar
            Column(
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Bs. ${cartItem.totalPrice.toInt()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = red
                )
            }
        }
    }
}