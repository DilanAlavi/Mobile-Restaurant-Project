package com.ucb.ucbtest.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.domain.Order
import com.ucb.usecases.order.GetOrdersHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersHistoryViewModel @Inject constructor(
    private val getOrdersHistoryUseCase: GetOrdersHistoryUseCase
) : ViewModel() {

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders

    fun loadOrders() {
        viewModelScope.launch {
            _orders.value = getOrdersHistoryUseCase()
        }
    }
}