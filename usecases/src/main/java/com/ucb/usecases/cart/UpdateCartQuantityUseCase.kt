package com.ucb.usecases.cart

import com.ucb.data.cart.ICartRepository
import javax.inject.Inject

class UpdateCartQuantityUseCase @Inject constructor(
    private val cartRepository: ICartRepository
) {
    suspend operator fun invoke(mealId: String, quantity: Int) {
        cartRepository.updateQuantity(mealId, quantity)
    }
}