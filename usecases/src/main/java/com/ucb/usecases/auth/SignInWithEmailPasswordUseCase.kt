package com.ucb.usecases.auth

import com.ucb.data.repository.AuthRepository
import com.ucb.domain.AuthError
import com.ucb.domain.User
import javax.inject.Inject

class SignInWithEmailPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(AuthError.InvalidCredentials)
        }

        return authRepository.signInWithEmailPassword(email, password)
    }
}