package com.ucb.ucbtest.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel : ViewModel() {

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

}