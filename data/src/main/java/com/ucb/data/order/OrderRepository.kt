package com.ucb.data.order

import com.ucb.domain.Order
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val remoteDataSource: IOrderRepository
) {
    suspend fun saveOrder(order: Order) {
        remoteDataSource.saveOrder(order)
    }

    suspend fun getAllOrders(): List<Order> {
        return remoteDataSource.getAllOrders()
    }

    suspend fun clearOrders() {
        remoteDataSource.clearOrders()
    }
}