package com.ucb.ucbtest.cart
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.domain.CartItem
import com.ucb.domain.Meal
import com.ucb.usecases.cart.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.ucb.ucbtest.service.Util
@HiltViewModel
class CartViewModel @Inject constructor(
    private val addToCartUseCase: AddToCartUseCase,
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase,
    private val removeFromCartUseCase: RemoveFromCartUseCase,
    private val getCartTotalUseCase: GetCartTotalUseCase,
    private val clearCartUseCase: ClearCartUseCase
) : ViewModel() {

    data class CartState(
        val items: List<CartItem> = emptyList(),
        val total: Double = 0.0,
        val itemCount: Int = 0,
        val isEmpty: Boolean = true
    )

    private val _state = MutableStateFlow(CartState())
    val state: StateFlow<CartState> = _state

    init {
        observeCart()
    }

    private fun observeCart() {
        viewModelScope.launch {
            combine(
                getCartItemsUseCase(),
                getCartTotalUseCase()
            ) { items, total ->
                CartState(
                    items = items,
                    total = total,
                    itemCount = items.sumOf { it.quantity },
                    isEmpty = items.isEmpty()
                )
            }.collect {
                _state.value = it
            }
        }
    }

    fun addToCart(meal: Meal, context: Context) {
        viewModelScope.launch {
            addToCartUseCase(meal)
            Util.sendNotificacionCarrito(
                context,
                "Producto agregado",
                "'${meal.strMeal}' fue añadido al carrito"
            )
        }
    }

    fun updateQuantity(mealId: String, quantity: Int) {
        viewModelScope.launch {
            updateCartQuantityUseCase(mealId, quantity)
        }
    }

    fun removeFromCart(mealId: String) {
        viewModelScope.launch {
            removeFromCartUseCase(mealId)
        }
    }
    fun clearCart() {
        viewModelScope.launch {
            clearCartUseCase()
            refreshCart()
        }
    }
    private fun refreshCart() {
        observeCart()
    }
}