package com.ucb.usecases.auth

import com.ucb.data.repository.AuthRepository
import com.ucb.domain.User
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): User? {
        return authRepository.getCurrentUser()
    }
}