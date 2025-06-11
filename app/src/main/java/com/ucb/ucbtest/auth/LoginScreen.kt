package com.ucb.ucbtest.auth

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.ucb.ucbtest.R
import com.ucb.ucbtest.auth.AuthState
import com.ucb.ucbtest.auth.AuthViewModel
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    onLoginSuccess: () -> Unit,
    viewModel: AuthViewModel
) {
    println("🔍 LoginScreen: AuthViewModel instance: ${viewModel.hashCode()}")
    val redColor = Color(0xFFB71C1C)
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    // ✅ Variables derivadas para evitar smart cast issues
    val isLoading = authState is AuthState.Loading
    val isEnabled = !isLoading

    // Configurar Google Sign-In
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.web_client_id))
            .requestEmail()
            .build()
    }

    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }



// En LoginScreen.kt - actualizar el googleSignInLauncher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            println("✅ GoogleSignIn: Account obtenido - ${account.email}")

            val idToken = account.idToken
            if (idToken != null) {
                println("✅ GoogleSignIn: ID Token obtenido, llamando ViewModel")
                println("🚀 GoogleSignIn: Token length: ${idToken.length}")
                viewModel.signInWithGoogle(idToken)
            } else {
                println("❌ GoogleSignIn: ID Token es null")
                Toast.makeText(context, "No se pudo obtener el token", Toast.LENGTH_LONG).show()
            }
        } catch (e: ApiException) {
            println("❌ GoogleSignIn: Error ${e.statusCode}: ${e.localizedMessage}")
            // ... resto del manejo de errores
        }
    }


    LaunchedEffect(authState) {
        val state = authState
        when (state) {
            is AuthState.Error -> {
                Toast.makeText(context, state.message, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
            else -> { /* No hacer nada, MainActivity maneja la navegación */ }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Logo y título
        Text(
            text = "Gusto",
            color = redColor,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Total",
            color = redColor,
            fontSize = 36.sp,
            fontWeight = FontWeight.Light,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Bienvenido",
            color = Color.Black,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Inicie sesión para acceder a su cuenta",
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Campo de email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Introduzca su correo electrónico") },
            leadingIcon = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = redColor
            ),
            enabled = isEnabled // ✅ Usar variable derivada
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = redColor
            ),
            enabled = isEnabled // ✅ Usar variable derivada
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Checkbox y "¿Has olvidado tu contraseña?"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(checkedColor = redColor),
                    enabled = isEnabled // ✅ Usar variable derivada
                )
                Text(
                    text = "Recuerdame",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            TextButton(
                onClick = { /* Handle forgot password */ },
                enabled = isEnabled // ✅ Usar variable derivada
            ) {
                Text(
                    text = "¿Has olvidado tu contraseña?",
                    fontSize = 12.sp,
                    color = redColor
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón Next (para email/password)
        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    viewModel.signInWithEmailPassword(email, password) // ← AGREGAR ESTA LLAMADA
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = redColor
            ),
            shape = RoundedCornerShape(8.dp),
            enabled = isEnabled && email.isNotBlank() && password.isNotBlank() // ✅ Usar variable derivada
        ) {
            if (isLoading) { // ✅ Usar variable derivada
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = "Next",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = ">",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Separador OR
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color.LightGray
            )
            Text(
                text = " OR ",
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Google Sign In
        OutlinedButton(
            onClick = {
                // Limpiar cuenta anterior si existe
                googleSignInClient.signOut()
                val signInIntent = googleSignInClient.signInIntent
                googleSignInLauncher.launch(signInIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                width = 1.dp,
                brush = androidx.compose.ui.graphics.SolidColor(Color.LightGray)
            ),
            shape = RoundedCornerShape(8.dp),
            enabled = isEnabled // ✅ Usar variable derivada
        ) {
            if (isLoading) { // ✅ Usar variable derivada
                CircularProgressIndicator(
                    color = redColor,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google_official),
                    contentDescription = "Google",
                    modifier = Modifier.size(20.dp),
                    tint = Color.Unspecified // ⚠️ MUY IMPORTANTE para mantener los colores
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Continuar con Google",
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }



        // Texto de registro
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿Es nuevo? ",
                color = Color.Gray,
                fontSize = 14.sp
            )
            TextButton(
                onClick = {
                    navController.navigate("register_screen")
                },
                enabled = isEnabled,
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
            ) {
                Text(
                    text = "Regístrese ahora",
                    color = redColor,
                    fontSize = 14.sp
                )
            }
        }
    }
}