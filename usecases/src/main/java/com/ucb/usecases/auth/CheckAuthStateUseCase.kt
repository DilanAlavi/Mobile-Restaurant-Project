package com.ucb.usecases.auth

import com.ucb.data.repository.AuthRepository
import javax.inject.Inject

class CheckAuthStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.isUserSignedIn()
    }
}