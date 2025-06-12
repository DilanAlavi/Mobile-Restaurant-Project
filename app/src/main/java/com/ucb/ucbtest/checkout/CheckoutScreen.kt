package com.ucb.ucbtest.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ucb.domain.CartItem
import com.ucb.ucbtest.R
import com.ucb.ucbtest.cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel = hiltViewModel(),
    checkoutViewModel: CheckoutViewModel = hiltViewModel()
) {
    val red = Color(0xFFC71818)
    val darkRed = Color(0xFFA01111)
    val lightPink = Color(0xFFF7E9E9)
    val gray = Color(0xFF8E8A8A)

    val cartState by cartViewModel.state.collectAsState()
    val checkoutState by checkoutViewModel.state.collectAsState()

    var selectedPaymentMethod by remember { mutableStateOf("Credit/Debit/ATM cards") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val paymentMethods = listOf(
        "Credit/Debit/ATM cards",
        "UPI",
        "Billetera",
        "Banca por internet"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Orden",
                        color = darkRed,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = darkRed
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Resumen de la orden
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    cartState.items.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.meal.strMeal} (${item.quantity}x)",
                                color = gray,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Bs. ${item.totalPrice.toInt()}",
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("GST", color = gray, fontSize = 14.sp)
                        Text("Bs. 5", color = Color.Black, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Pago por delivery", color = gray, fontSize = 14.sp)
                        Text("Bs. 25", color = Color.Black, fontSize = 14.sp)
                    }

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "TOTAL",
                            color = red,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Bs. ${(cartState.total + 30).toInt()}",
                            color = red,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Pagar por",
                color = red,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            paymentMethods.forEach { method ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedPaymentMethod = method }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedPaymentMethod == method,
                        onClick = { selectedPaymentMethod = method },
                        colors = RadioButtonDefaults.colors(selectedColor = red)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = method,
                        fontSize = 14.sp,
                        color = if (selectedPaymentMethod == method) red else gray
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    checkoutViewModel.processOrder(
                        cartItems = cartState.items,
                        paymentMethod = selectedPaymentMethod
                    )
                    showSuccessDialog = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = red),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Continuar",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }

    // Dialog de éxito con imagen
    if (showSuccessDialog) {
        Dialog(onDismissRequest = { }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "🚚 Tu pedido está en camino",
                        color = Color(0xFFC71818),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Imagen tipo mapa
                    Image(
                        painter = painterResource(id = R.drawable.map_placeholder),
                        contentDescription = "Mapa de ejemplo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            showSuccessDialog = false
                            cartViewModel.clearCart()
                            navController.navigate("meal_screen") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC71818)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "ACEPTAR",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
