package com.ucb.usecases.cart

import com.ucb.data.cart.ICartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartTotalUseCase @Inject constructor(
    private val cartRepository: ICartRepository
) {
    operator fun invoke(): Flow<Double> {
        return cartRepository.getCartTotal()
    }
}