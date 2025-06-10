package com.ucb.data.order

import com.ucb.domain.Order

interface IOrderRepository {
    suspend fun saveOrder(order: Order)
    suspend fun getAllOrders(): List<Order>
    suspend fun clearOrders()
}