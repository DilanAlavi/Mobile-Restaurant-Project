package com.ucb.framework.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.ucb.data.repository.AuthRepository
import com.ucb.domain.AuthError
import com.ucb.domain.User
import com.ucb.framework.mappers.toAuthError
import com.ucb.framework.mappers.toUser
import kotlinx.coroutines.tasks.await

// ✅ SIN anotaciones de Hilt
class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth // ✅ Constructor normal
) : AuthRepository {

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = result.user

            if (firebaseUser != null) {
                val user = firebaseUser.toUser()
                Result.success(user)
            } else {
                Result.failure(AuthError.InvalidCredentials)
            }
        } catch (e: FirebaseAuthException) {
            Result.failure(e.toAuthError())
        } catch (e: Exception) {
            Result.failure(AuthError.Unknown(e.message))
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(AuthError.Unknown(e.message))
        }
    }

    override suspend fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.toUser()
    }

    override suspend fun isUserSignedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}