package com.ucb.framework.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.ucb.data.repository.AuthRepository
import com.ucb.domain.AuthError
import com.ucb.domain.User
import com.ucb.framework.auth.FakeUserManager
import com.ucb.framework.mappers.toAuthError
import com.ucb.framework.mappers.toUser
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val fakeUserManager: FakeUserManager // ✅ AGREGAR ESTE PARÁMETRO
) : AuthRepository {

    override suspend fun signInWithGoogle(idToken: String): Result<User> {
        return try {
            println("🔥 AuthRepositoryImpl: Iniciando signInWithGoogle")
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = result.user

            if (firebaseUser != null) {
                val user = firebaseUser.toUser()
                println("✅ AuthRepositoryImpl: Login exitoso - User: ${user.name} (${user.email})")
                Result.success(user)
            } else {
                println("❌ AuthRepositoryImpl: FirebaseUser es null")
                Result.failure(AuthError.InvalidCredentials)
            }
        } catch (e: FirebaseAuthException) {
            println("❌ AuthRepositoryImpl: FirebaseAuthException - ${e.message}")
            Result.failure(e.toAuthError())
        } catch (e: Exception) {
            println("❌ AuthRepositoryImpl: Exception - ${e.message}")
            Result.failure(AuthError.Unknown(e.message))
        }
    }

    override suspend fun signInWithEmailPassword(email: String, password: String): Result<User> {
        return try {
            println("🔥 AuthRepositoryImpl: Intentando login con email/password: $email")

            val fakeUser = fakeUserManager.authenticate(email, password)
            if (fakeUser != null) {
                println("✅ AuthRepositoryImpl: Login fake exitoso para ${fakeUser.name}")
                return Result.success(fakeUser)
            }

            println("🔥 AuthRepositoryImpl: No es usuario fake, intentando con Firebase")
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user

            if (firebaseUser != null) {
                val user = firebaseUser.toUser()
                println("✅ AuthRepositoryImpl: Login Firebase exitoso - User: ${user.name} (${user.email})")
                Result.success(user)
            } else {
                println("❌ AuthRepositoryImpl: FirebaseUser es null")
                Result.failure(AuthError.InvalidCredentials)
            }
        } catch (e: FirebaseAuthException) {
            println("❌ AuthRepositoryImpl: FirebaseAuthException - ${e.message}")
            Result.failure(e.toAuthError())
        } catch (e: Exception) {
            println("❌ AuthRepositoryImpl: Exception - ${e.message}")
            Result.failure(AuthError.Unknown(e.message))
        }
    }

    override suspend fun registerWithEmailPassword(email: String, password: String, name: String): Result<User> {
        return try {
            println("🔥 AuthRepositoryImpl: Intentando registro con email/password: $email")

            if (email.isBlank() || password.isBlank() || name.isBlank()) {
                return Result.failure(AuthError.InvalidCredentials)
            }

            if (password.length < 6) {
                return Result.failure(AuthError.InvalidCredentials)
            }

            val existingFakeUsers = fakeUserManager.getAllUsers()
            if (existingFakeUsers.containsKey(email.lowercase())) {
                println("❌ AuthRepositoryImpl: Usuario fake ya existe: $email")
                return Result.failure(AuthError.AccountExistsWithDifferentCredential)
            }

            val newUser = User(
                id = "fake_${System.currentTimeMillis()}",
                name = name.trim(),
                email = email.lowercase(),
                photoUrl = "https://i.pravatar.cc/150?img=${(1..50).random()}", // Avatar aleatorio
                isEmailVerified = true
            )

            val success = fakeUserManager.addUser(email.lowercase(), password, newUser)

            if (success) {
                println("✅ AuthRepositoryImpl: Usuario fake registrado exitosamente: ${newUser.name} (${email})")
                Result.success(newUser)
            } else {
                println("❌ AuthRepositoryImpl: Error al agregar usuario fake")
                Result.failure(AuthError.Unknown("Error al registrar usuario"))
            }

        } catch (e: Exception) {
            println("❌ AuthRepositoryImpl: Exception en registro - ${e.message}")
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

    // ✅ MÉTODOS ADICIONALES PARA GESTIÓN DE USUARIOS FAKE
    suspend fun addFakeUser(email: String, password: String, user: User): Boolean {
        return fakeUserManager.addUser(email, password, user)
    }

    suspend fun updateFakeUser(email: String, newPassword: String? = null, newUser: User? = null): Boolean {
        return fakeUserManager.updateUser(email, newPassword, newUser)
    }

    suspend fun deleteFakeUser(email: String): Boolean {
        return fakeUserManager.deleteUser(email)
    }

    suspend fun getAllFakeUsers(): Map<String, Pair<String, User>> {
        return fakeUserManager.getAllUsers()
    }

    suspend fun resetFakeUsers() {
        fakeUserManager.resetToDefaults()
    }

    suspend fun changeFakeUserPassword(email: String, oldPassword: String, newPassword: String): Boolean {
        return fakeUserManager.changePassword(email, oldPassword, newPassword)
    }
}