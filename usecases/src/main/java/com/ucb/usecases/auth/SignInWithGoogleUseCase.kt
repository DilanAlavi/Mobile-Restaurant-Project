package com.ucb.usecases.auth

import com.ucb.data.repository.AuthRepository
import com.ucb.domain.AuthError
import com.ucb.domain.User
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<User> {
        if (idToken.isBlank()) {
            return Result.failure(AuthError.InvalidCredentials)
        }

        return authRepository.signInWithGoogle(idToken)
    }
}