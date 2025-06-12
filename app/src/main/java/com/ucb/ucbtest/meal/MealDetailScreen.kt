package com.ucb.ucbtest.meal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ucb.domain.Meal
import com.ucb.ucbtest.cart.CartViewModel

@Composable
fun MealDetailScreen(
    meal: Meal,
    navController: NavController
) {
    val cartViewModel: CartViewModel = hiltViewModel()
    val red = Color(0xFFC71818)
    val darkRed = Color(0xFFA01111)
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Imagen
        AsyncImage(
            model = meal.strMealThumb,
            contentDescription = meal.strMeal,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Nombre del plato
        Text(
            text = meal.strMeal,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = darkRed
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Categoría y Área
        Text(
            text = "${meal.strCategory} • ${meal.strArea}",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                color = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Bs. ${meal.price.toInt()}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = red,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sección de instrucciones
        Text(
            text = "Instrucciones",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF222222)
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = meal.strInstructions,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                color = Color(0xFF444444)
            ),
            modifier = Modifier.padding(horizontal = 16.dp),
            textAlign = TextAlign.Justify
        )

        Spacer(modifier = Modifier.height(32.dp))

        // BOTONES ARREGLADOS
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Botón Añadir al carrito
            Button(
                onClick = {
                    cartViewModel.addToCart(meal,context)
                    Toast.makeText(context, "'${meal.strMeal}' fue añadido al carrito", Toast.LENGTH_SHORT).show()
                    navController.navigate("cart")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = red),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Añadir al carrito",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            // Botón Proceder al pago - ARREGLADO
            Button(
                onClick = {
                    cartViewModel.addToCart(meal,context)
                    navController.navigate("cart")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 2.dp,
                        color = red,
                        shape = RoundedCornerShape(12.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = red
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    "Proceder al pago",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }
}