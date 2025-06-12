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
import com.ucb.framework.auth.InternalUserManager
import com.ucb.framework.mappers.toAuthError
import com.ucb.framework.mappers.toUser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val internalUserManager: InternalUserManager, // ✅ CAMBIO DE NOMBRE
    @ApplicationContext private val context: Context,
    private val gson: Gson
) : AuthRepository {

    private val sessionPrefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE) // ✅ NOMBRE MÁS PROFESIONAL

    private val currentLocalUserKey = "current_local_user" // ✅ CAMBIO DE NOMBRE

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

            // ✅ VERIFICAR USUARIOS INTERNOS PRIMERO
            val localUser = internalUserManager.authenticate(email, password)
            if (localUser != null) {
                saveLocalUserSession(localUser)

                println("✅ AuthRepositoryImpl: Login interno exitoso para ${localUser.name}")
                return Result.success(localUser)
            }

            println("🔥 AuthRepositoryImpl: No es usuario interno, intentando con Firebase")
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user

            if (firebaseUser != null) {
                val user = firebaseUser.toUser()

                clearLocalUserSession()

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

            val existingLocalUsers = internalUserManager.getAllUsers()
            if (existingLocalUsers.containsKey(email.lowercase())) {
                println("❌ AuthRepositoryImpl: Usuario interno ya existe: $email")
                return Result.failure(AuthError.AccountExistsWithDifferentCredential)
            }

            val newUser = User(
                id = "local_${System.currentTimeMillis()}", // ✅ CAMBIO DE PREFIJO
                name = name.trim(),
                email = email.lowercase(),
                photoUrl = "https://i.pravatar.cc/150?img=${(1..50).random()}", // Avatar aleatorio
                isEmailVerified = true
            )

            val success = internalUserManager.addUser(email.lowercase(), password, newUser)

            if (success) {
                saveLocalUserSession(newUser)

                println("✅ AuthRepositoryImpl: Usuario interno registrado exitosamente: ${newUser.name} (${email})")
                Result.success(newUser)
            } else {
                println("❌ AuthRepositoryImpl: Error al agregar usuario interno")
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
            clearLocalUserSession()

            println("✅ AuthRepositoryImpl: SignOut exitoso - todas las sesiones limpiadas")
            Result.success(Unit)
        } catch (e: Exception) {
            println("❌ AuthRepositoryImpl: Error en signOut - ${e.message}")
            Result.failure(AuthError.Unknown(e.message))
        }
    }

    override suspend fun getCurrentUser(): User? {
        return try {
            // 1. Primero revisar sesión local
            val localUser = getCurrentLocalUser()
            if (localUser != null) {
                println("🔍 AuthRepositoryImpl: getCurrentUser - Usuario local encontrado: ${localUser.email}")
                return localUser
            }

            // 2. Si no hay local, revisar Firebase
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

    // ✅ MÉTODOS PARA MANEJO DE SESIÓN LOCAL
    private fun saveLocalUserSession(user: User) {
        try {
            val userJson = gson.toJson(user)
            sessionPrefs.edit().putString(currentLocalUserKey, userJson).apply()
            println("✅ AuthRepositoryImpl: Sesión local guardada para ${user.email}")
        } catch (e: Exception) {
            println("❌ AuthRepositoryImpl: Error guardando sesión local - ${e.message}")
        }
    }

    private fun getCurrentLocalUser(): User? {
        return try {
            val userJson = sessionPrefs.getString(currentLocalUserKey, null)
            if (userJson != null) {
                gson.fromJson(userJson, User::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            println("❌ AuthRepositoryImpl: Error obteniendo usuario local actual - ${e.message}")
            null
        }
    }

    private fun clearLocalUserSession() {
        try {
            sessionPrefs.edit().remove(currentLocalUserKey).apply()
            println("✅ AuthRepositoryImpl: Sesión local limpiada")
        } catch (e: Exception) {
            println("❌ AuthRepositoryImpl: Error limpiando sesión local - ${e.message}")
        }
    }

    // ✅ MÉTODOS PARA GESTIÓN DE USUARIOS INTERNOS
    suspend fun addInternalUser(email: String, password: String, user: User): Boolean {
        return internalUserManager.addUser(email, password, user)
    }

    suspend fun updateInternalUser(email: String, newPassword: String? = null, newUser: User? = null): Boolean {
        return internalUserManager.updateUser(email, newPassword, newUser)
    }

    suspend fun deleteInternalUser(email: String): Boolean {
        return internalUserManager.deleteUser(email)
    }

    suspend fun getAllInternalUsers(): Map<String, Pair<String, User>> {
        return internalUserManager.getAllUsers()
    }

    suspend fun resetInternalUsers() {
        internalUserManager.resetToDefaults()
    }

    suspend fun changeInternalUserPassword(email: String, oldPassword: String, newPassword: String): Boolean {
        return internalUserManager.changePassword(email, oldPassword, newPassword)
    }
}