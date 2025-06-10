package com.ucb.ucbtest.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.domain.CartItem
import com.ucb.domain.Order
import com.ucb.usecases.cart.ClearCartUseCase
import com.ucb.usecases.order.SaveOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val saveOrderUseCase: SaveOrderUseCase
) : ViewModel() {

    data class CheckoutState(
        val isProcessing: Boolean = false,
        val isSuccess: Boolean = false,
        val error: String? = null
    )

    private val _state = MutableStateFlow(CheckoutState())
    val state: StateFlow<CheckoutState> = _state

    fun processOrder(cartItems: List<CartItem>, paymentMethod: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isProcessing = true)

            try {
                val subtotal = cartItems.sumOf { it.totalPrice }
                val order = Order(
                    id = UUID.randomUUID().toString(),
                    items = cartItems,
                    subtotal = subtotal,
                    paymentMethod = paymentMethod
                )

                saveOrderUseCase(order)
                _state.value = _state.value.copy(isProcessing = false, isSuccess = true)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isProcessing = false,
                    error = e.message
                )
            }
        }
    }
}
