package com.ucb.usecases.cart

import com.ucb.data.cart.ICartRepository
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(
    private val cartRepository: ICartRepository
) {
    suspend operator fun invoke() {
        cartRepository.clearCart()
    }
}