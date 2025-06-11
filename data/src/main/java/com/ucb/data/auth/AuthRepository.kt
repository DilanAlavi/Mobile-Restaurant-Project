package com.ucb.data.repository

import com.ucb.domain.AuthError
import com.ucb.domain.User

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun signInWithEmailPassword(email: String, password: String): Result<User>
    suspend fun registerWithEmailPassword(email: String, password: String, name: String): Result<User> // ✅ AGREGAR ESTA LÍNEA
    suspend fun signOut(): Result<Unit>
    suspend fun getCurrentUser(): User?
    suspend fun isUserSignedIn(): Boolean
}