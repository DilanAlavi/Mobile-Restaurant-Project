package com.ucb.ucbtest.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.domain.AuthError
import com.ucb.ucbtest.auth.AuthState
import com.ucb.usecases.auth.CheckAuthStateUseCase
import com.ucb.usecases.auth.GetCurrentUserUseCase
import com.ucb.usecases.auth.SignInWithGoogleUseCase
import com.ucb.usecases.auth.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val signInWithGoogleUseCase: SignInWithGoogleUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val checkAuthStateUseCase: CheckAuthStateUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkInitialAuthState()
    }

    private fun checkInitialAuthState() {
        viewModelScope.launch {
            if (checkAuthStateUseCase()) {
                val user = getCurrentUserUseCase()
                _authState.value = if (user != null) {
                    AuthState.Authenticated(user)
                } else {
                    AuthState.Unauthenticated
                }
            } else {
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    // app/src/main/java/com/ucb/ucbtest/viewmodel/AuthViewModel.kt
    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            println("🚀 AuthViewModel: Iniciando signInWithGoogle con token")
            _authState.value = AuthState.Loading
            println("🔄 AuthViewModel: Estado cambiado a Loading")

            signInWithGoogleUseCase(idToken)
                .onSuccess { user ->
                    println("✅ AuthViewModel: UseCase retornó éxito - User: ${user.name} (${user.email})")
                    _authState.value = AuthState.Authenticated(user)
                    println("✅ AuthViewModel: Estado cambiado a Authenticated")
                    println("🔍 AuthViewModel: Estado actual: ${_authState.value}")
                }
                .onFailure { error ->
                    println("❌ AuthViewModel: UseCase retornó falla - ${error.message}")
                    _authState.value = AuthState.Error(error.toUserMessage())
                    println("❌ AuthViewModel: Estado cambiado a Error")
                }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            signOutUseCase()
                .onSuccess {
                    _authState.value = AuthState.Unauthenticated
                }
                .onFailure { error ->
                    _authState.value = AuthState.Error(error.toUserMessage())
                }
        }
    }

    fun clearError() {
        if (_authState.value is AuthState.Error) {
            _authState.value = AuthState.Unauthenticated
        }
    }

    private fun Throwable.toUserMessage(): String {
        return when (this) {
            is AuthError.NetworkError -> "Sin conexión a internet"
            is AuthError.InvalidCredentials -> "Credenciales inválidas"
            is AuthError.AccountDisabled -> "Cuenta deshabilitada"
            is AuthError.AccountExistsWithDifferentCredential -> "Ya existe una cuenta con este email"
            is AuthError.CredentialAlreadyInUse -> "Credencial ya está en uso"
            is AuthError.UserCancelled -> "Operación cancelada"
            is AuthError.Unknown -> message ?: "Error desconocido"
            else -> "Error inesperado"
        }
    }
}