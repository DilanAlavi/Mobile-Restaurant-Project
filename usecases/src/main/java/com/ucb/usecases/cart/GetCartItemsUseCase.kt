package com.ucb.usecases.cart

import com.ucb.data.cart.ICartRepository
import com.ucb.domain.CartItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val cartRepository: ICartRepository
) {
    operator fun invoke(): Flow<List<CartItem>> {
        return cartRepository.getCartItems()
    }
}