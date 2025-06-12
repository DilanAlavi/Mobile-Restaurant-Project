package com.ucb.framework.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import com.ucb.data.repository.AuthRepository
import com.ucb.domain.AuthError
import com.ucb.domain.User
import com.ucb.framework.auth.FakeUserManager
import com.ucb.framework.mappers.toAuthError
import com.ucb.framework.mappers.toUser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val fakeUserManager: FakeUserManager,
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : AuthRepository {

    private val sessionPrefs: SharedPreferences =
        context.getSharedPreferences("fake_user_session", Context.MODE_PRIVATE)

    private val currentFakeUserKey = "current_fake_user"

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
                saveFakeUserSession(fakeUser)

                println("✅ AuthRepositoryImpl: Login fake exitoso para ${fakeUser.name}")
                return Result.success(fakeUser)
            }

            println("🔥 AuthRepositoryImpl: No es usuario fake, intentando con Firebase")
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user

            if (firebaseUser != null) {
                val user = firebaseUser.toUser()

                clearFakeUserSession()

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
                saveFakeUserSession(newUser)

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
            clearFakeUserSession()

            println("✅ AuthRepositoryImpl: SignOut exitoso - todas las sesiones limpiadas")
            Result.success(Unit)
        } catch (e: Exception) {
            println("❌ AuthRepositoryImpl: Error en signOut - ${e.message}")
            Result.failure(AuthError.Unknown(e.message))
        }
    }

    override suspend fun getCurrentUser(): User? {
        return try {
            // 1. Primero revisar sesión fake
            val fakeUser = getCurrentFakeUser()
            if (fakeUser != null) {
                println("🔍 AuthRepositoryImpl: getCurrentUser - Usuario fake encontrado: ${fakeUser.email}")
                return fakeUser
            }

            // 2. Si no hay fake, revisar Firebase
            val firebaseUser = firebaseAuth.currentUser?.toUser()
            if (firebaseUser != null) {
                println("🔍 AuthRepositoryImpl: getCurrentUser - Usuario Firebase encontrado: ${firebaseUser.email}")
                return firebaseUser
            }

            println("🔍 AuthRepositoryImpl: getCurrentUser - No hay usuario autenticado")
            null
        } catch (e: Exception) {
            println("❌ AuthRepositoryImpl: Error en getCurrentUser - ${e.message}")
            null
        }
    }

    override suspend fun isUserSignedIn(): Boolean {
        return try {
            val hasCurrentUser = getCurrentUser() != null
            println("🔍 AuthRepositoryImpl: isUserSignedIn - $hasCurrentUser")
            hasCurrentUser
        } catch (e: Exception) {
            println("❌ AuthRepositoryImpl: Error en isUserSignedIn - ${e.message}")
            false
        }
    }

    private fun saveFakeUserSession(user: User) {
        try {
            val userJson = gson.toJson(user)
            sessionPrefs.edit().putString(currentFakeUserKey, userJson).apply()
            println("✅ AuthRepositoryImpl: Sesión fake guardada para ${user.email}")
        } catch (e: Exception) {
            println("❌ AuthRepositoryImpl: Error guardando sesión fake - ${e.message}")
        }
    }

    private fun getCurrentFakeUser(): User? {
        return try {
            val userJson = sessionPrefs.getString(currentFakeUserKey, null)
            if (userJson != null) {
                gson.fromJson(userJson, User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            println("❌ AuthRepositoryImpl: Error obteniendo usuario fake actual - ${e.message}")
            null
        }
    }

    private fun clearFakeUserSession() {
        try {
            sessionPrefs.edit().remove(currentFakeUserKey).apply()
            println("✅ AuthRepositoryImpl: Sesión fake limpiada")
        } catch (e: Exception) {
            println("❌ AuthRepositoryImpl: Error limpiando sesión fake - ${e.message}")
        }
    }

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