package com.ucb.usecases.cart

import com.ucb.data.cart.ICartRepository
import com.ucb.domain.Meal
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: ICartRepository
) {
    suspend operator fun invoke(meal: Meal) {
        cartRepository.addToCart(meal)
    }
}