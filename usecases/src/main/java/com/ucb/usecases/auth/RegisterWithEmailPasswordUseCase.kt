package com.ucb.usecases.auth

import com.ucb.data.repository.AuthRepository
import com.ucb.domain.AuthError
import com.ucb.domain.User
import javax.inject.Inject

class RegisterWithEmailPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    // ✅ REGEX SIMPLE PARA VALIDAR EMAIL (SIN ANDROID DEPENDENCIES)
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    suspend operator fun invoke(email: String, password: String, name: String): Result<User> {
        // ✅ VALIDACIONES DE ENTRADA
        if (email.isBlank()) {
            return Result.failure(AuthError.InvalidCredentials)
        }

        if (password.isBlank()) {
            return Result.failure(AuthError.InvalidCredentials)
        }

        if (name.isBlank()) {
            return Result.failure(AuthError.InvalidCredentials)
        }

        // ✅ VALIDAR FORMATO DE EMAIL (SIN ANDROID.UTIL.PATTERNS)
        if (!emailRegex.matches(email.trim())) {
            return Result.failure(AuthError.InvalidCredentials)
        }

        // ✅ VALIDAR LONGITUD DE CONTRASEÑA
        if (password.length < 6) {
            return Result.failure(AuthError.InvalidCredentials)
        }

        // ✅ VALIDAR LONGITUD DE NOMBRE
        if (name.trim().length < 2) {
            return Result.failure(AuthError.InvalidCredentials)
        }

        return authRepository.registerWithEmailPassword(email.trim(), password, name.trim())
    }
}