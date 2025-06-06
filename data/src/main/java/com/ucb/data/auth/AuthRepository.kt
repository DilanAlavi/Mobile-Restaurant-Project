package com.ucb.data.repository

import com.ucb.domain.AuthError
import com.ucb.domain.User

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun signOut(): Result<Unit>
    suspend fun getCurrentUser(): User?
    suspend fun isUserSignedIn(): Boolean
}