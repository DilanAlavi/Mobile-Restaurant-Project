package com.ucb.data.auth

import com.ucb.domain.User

interface AuthRepository {
    suspend fun signInWithGoogle(idToken: String): Result<User>
    suspend fun signOut(): Result<Unit>
    fun getCurrentUser(): User?
    fun isUserSignedIn(): Boolean
}