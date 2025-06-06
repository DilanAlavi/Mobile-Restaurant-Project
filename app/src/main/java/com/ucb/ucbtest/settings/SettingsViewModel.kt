package com.ucb.ucbtest.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.ucb.usecases.auth.SignOutUseCase
import com.ucb.usecases.auth.CheckAuthStateUseCase
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val checkAuthStateUseCase: CheckAuthStateUseCase
) : ViewModel() {

    // Aquí defines qué ítems se verán en Settings
    private val _settingsItems = MutableStateFlow(
        listOf(
            "Mi cuenta",
            "Filtros de comida",
            "Mis pedidos",
            "Mi carro de compras",
            "My Rewards",
            "Ofertas",
            "Reservas"
        )
    )
    val settingsItems: StateFlow<List<String>> = _settingsItems

    private val _otherItems = MutableStateFlow(
        listOf(
            "Preferencia de notificaciones",
            "Centro de ayuda",
            "Políticas de privacidad",
            "Legal"
        )
    )
    val otherItems: StateFlow<List<String>> = _otherItems

    // Estado para manejar el loading del logout
    private val _isLoggingOut = MutableStateFlow(false)
    val isLoggingOut: StateFlow<Boolean> = _isLoggingOut

    // ✅ Para forzar el reinicio de la app
    private val _shouldRestartApp = MutableStateFlow(false)
    val shouldRestartApp: StateFlow<Boolean> = _shouldRestartApp

    fun signOut() {
        viewModelScope.launch {
            _isLoggingOut.value = true
            println("🚪 SettingsViewModel: Iniciando logout...")

            try {
                signOutUseCase()
                    .onSuccess {
                        println("✅ SettingsViewModel: Logout exitoso")
                        _isLoggingOut.value = false

                        // ✅ Verificar que realmente se cerró sesión
                        try {
                            val isAuthenticated = checkAuthStateUseCase()
                            if (!isAuthenticated) {
                                println("🎯 SettingsViewModel: Logout confirmado, reiniciando app...")
                                _shouldRestartApp.value = true
                            } else {
                                println("⚠️ SettingsViewModel: Logout no confirmado")
                                // Fallback: forzar reinicio después de un delay
                                delay(1000)
                                _shouldRestartApp.value = true
                            }
                        } catch (e: Exception) {
                            println("⚠️ SettingsViewModel: Error verificando estado: ${e.message}")
                            // Fallback: forzar reinicio
                            _shouldRestartApp.value = true
                        }
                    }
                    .onFailure { error ->
                        println("❌ SettingsViewModel: Error en logout: ${error.message}")
                        _isLoggingOut.value = false
                    }
            } catch (e: Exception) {
                println("❌ SettingsViewModel: Excepción en logout: ${e.message}")
                _isLoggingOut.value = false
            }
        }
    }

    fun onRestartHandled() {
        _shouldRestartApp.value = false
    }
}