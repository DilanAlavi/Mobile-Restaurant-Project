package com.ucb.usecases.cart

import com.ucb.data.cart.ICartRepository
import javax.inject.Inject

class RemoveFromCartUseCase @Inject constructor(
    private val cartRepository: ICartRepository
) {
    suspend operator fun invoke(mealId: String) {
        cartRepository.removeFromCart(mealId)
    }
}