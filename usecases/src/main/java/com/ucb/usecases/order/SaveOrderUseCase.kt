package com.ucb.usecases.order

import com.ucb.data.order.IOrderRepository
import com.ucb.domain.Order
import javax.inject.Inject

class SaveOrderUseCase @Inject constructor(
    private val orderRepository: IOrderRepository
) {
    suspend operator fun invoke(order: Order) {
        orderRepository.saveOrder(order)
    }
}