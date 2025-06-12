package com.ucb.ucbtest.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ucb.domain.User
import com.ucb.ucbtest.auth.AuthState
import com.ucb.ucbtest.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    val red = Color(0xFFC71818)
    val darkRed = Color(0xFFA01111)
    val lightGray = Color(0xFFF5F5F5)

    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    // Obtener usuario actual
    val currentUser = when (val state = authState) {
        is AuthState.Authenticated -> state.user
        else -> null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Perfil",
                        color = darkRed,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->

        if (currentUser != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(lightGray)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // Información del usuario principal
                UserInfoCard(user = currentUser, red = red, darkRed = darkRed)

                Spacer(modifier = Modifier.height(24.dp))

                // Opciones del perfil
                ProfileOptionsCard(
                    navController = navController,
                    authViewModel = authViewModel,
                    red = red,
                    darkRed = darkRed
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Información adicional
                AdditionalInfoCard(user = currentUser, red = red, darkRed = darkRed)

                Spacer(modifier = Modifier.height(24.dp))
            }
        } else {
            // Estado de carga o error
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = red)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Cargando perfil...")
                }
            }
        }
    }
}

@Composable
fun UserInfoCard(
    user: User,
    red: Color,
    darkRed: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar del usuario (siempre el mismo ícono por defecto)
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(red.copy(alpha = 0.1f))
                    .border(3.dp, red, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(50.dp),
                    tint = red
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre del usuario
            Text(
                text = user.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = darkRed,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email del usuario con estado de verificación
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = user.email,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                if (user.isEmailVerified) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Email verificado",
                        modifier = Modifier.size(20.dp),
                        tint = Color(0xFF4CAF50) // Verde
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Estado de verificación
            Surface(
                color = if (user.isEmailVerified)
                    Color(0xFF4CAF50).copy(alpha = 0.1f)
                else
                    Color(0xFFF44336).copy(alpha = 0.1f),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = if (user.isEmailVerified) "Email Verificado" else "Email No Verificado",
                    fontSize = 14.sp,
                    color = if (user.isEmailVerified) Color(0xFF4CAF50) else Color(0xFFF44336),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun ProfileOptionsCard(
    navController: NavController,
    authViewModel: AuthViewModel,
    red: Color,
    darkRed: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Opciones",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = darkRed,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ProfileOption(
                icon = Icons.Filled.List,
                title = "Historial de Pedidos",
                subtitle = "Ver mis pedidos anteriores",
                onClick = { navController.navigate("orders_history") },
                red = red
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            ProfileOption(
                icon = Icons.Filled.Info,
                title = "Ayuda y Soporte",
                subtitle = "¿Necesitas ayuda?",
                onClick = { /* TODO: Implementar ayuda */ },
                red = red
            )

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            ProfileOption(
                icon = Icons.Filled.ExitToApp,
                title = "Cerrar Sesión",
                subtitle = "Salir de mi cuenta",
                onClick = { authViewModel.signOut() },
                red = Color.Red,
                isDestructive = true
            )
        }
    }
}

@Composable
fun AdditionalInfoCard(
    user: User,
    red: Color,
    darkRed: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Información de la Cuenta",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = darkRed,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            InfoRow(
                label = "ID de Usuario:",
                value = user.id.take(8) + "...",
                icon = Icons.Filled.AccountBox
            )

            InfoRow(
                label = "Estado del Email:",
                value = if (user.isEmailVerified) "Verificado" else "No verificado",
                icon = Icons.Filled.Email
            )

            InfoRow(
                label = "Foto de Perfil:",
                value = if (!user.photoUrl.isNullOrEmpty()) "Configurada" else "Sin configurar",
                icon = Icons.Filled.AccountCircle
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileOption(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    red: Color,
    isDestructive: Boolean = false
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isDestructive) Color.Red else red,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDestructive) Color.Red else Color.Black
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Ir",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

// Funciones auxiliares simplificadas (UserType y timestamp no disponibles en tu User actual)