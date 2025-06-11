package com.ucb.framework.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ucb.domain.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// ✅ EXTENSIÓN PARA CREAR DATASTORE
private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "fake_users")

// ✅ CLASE DE DATOS SIMPLE PARA GSON (EN LUGAR DE PAIR)
data class FakeUserEntry(
    val password: String,
    val user: User
)

@Singleton
class FakeUserManager @Inject constructor(
    private val context: Context,
    private val gson: Gson = Gson()
) {

    private val USERS_KEY = stringPreferencesKey("fake_users")

    // ✅ USUARIOS POR DEFECTO (NUEVA ESTRUCTURA)
    private val defaultUsers = mapOf(
        "admin@ucb.edu.bo" to FakeUserEntry("admin123", User("admin_001", "Administrador UCB", "admin@ucb.edu.bo", "https://i.pravatar.cc/150?img=1", true)),
        "student@ucb.edu.bo" to FakeUserEntry("student123", User("student_001", "Estudiante UCB", "student@ucb.edu.bo", "https://i.pravatar.cc/150?img=2", true)),
        "teacher@ucb.edu.bo" to FakeUserEntry("teacher123", User("teacher_001", "Docente UCB", "teacher@ucb.edu.bo", "https://i.pravatar.cc/150?img=3", true)),
        "guest@ucb.edu.bo" to FakeUserEntry("guest123", User("guest_001", "Invitado UCB", "guest@ucb.edu.bo", "https://i.pravatar.cc/150?img=4", false)),
        "test@ucb.edu.bo" to FakeUserEntry("test123", User("test_001", "Usuario Test", "test@ucb.edu.bo", "https://i.pravatar.cc/150?img=5", true))
    )

    // ✅ OBTENER TODOS LOS USUARIOS (NUEVA ESTRUCTURA)
    suspend fun getAllUsers(): Map<String, Pair<String, User>> {
        return try {
            context.userDataStore.data.map { preferences ->
                val usersJson = preferences[USERS_KEY]
                if (usersJson != null) {
                    try {
                        // ✅ DESERIALIZAR ESTRUCTURA SIMPLE
                        val type = object : TypeToken<Map<String, FakeUserEntry>>() {}.type
                        val userEntries: Map<String, FakeUserEntry> = gson.fromJson(usersJson, type) ?: emptyMap()

                        // ✅ CONVERTIR A FORMATO LEGACY PARA COMPATIBILIDAD
                        userEntries.mapValues { (_, entry) ->
                            Pair(entry.password, entry.user)
                        }
                    } catch (e: Exception) {
                        // Si hay error al deserializar, usar usuarios por defecto
                        println("⚠️ Error deserializando usuarios: ${e.message}, usando defaults")
                        clearCorruptedData()
                        defaultUsersAsLegacyFormat()
                    }
                } else {
                    // Primera vez - usar usuarios por defecto
                    saveUsers(defaultUsers)
                    defaultUsersAsLegacyFormat()
                }
            }.first()
        } catch (e: Exception) {
            println("❌ Error crítico en getAllUsers: ${e.message}")
            // Retornar usuarios por defecto como fallback
            defaultUsersAsLegacyFormat()
        }
    }

    // ✅ CONVERTIR USUARIOS POR DEFECTO A FORMATO LEGACY
    private fun defaultUsersAsLegacyFormat(): Map<String, Pair<String, User>> {
        return defaultUsers.mapValues { (_, entry) ->
            Pair(entry.password, entry.user)
        }
    }

    // ✅ LIMPIAR DATOS CORRUPTOS
    private suspend fun clearCorruptedData() {
        try {
            context.userDataStore.edit { preferences ->
                preferences.clear()
            }
            println("🧹 Datos corruptos limpiados")
        } catch (e: Exception) {
            println("❌ Error limpiando datos: ${e.message}")
        }
    }

    // ✅ GUARDAR USUARIOS (NUEVA ESTRUCTURA)
    private suspend fun saveUsers(users: Map<String, FakeUserEntry>) {
        try {
            val usersJson = gson.toJson(users)
            context.userDataStore.edit { preferences ->
                preferences[USERS_KEY] = usersJson
            }
            println("💾 Usuarios guardados correctamente")
        } catch (e: Exception) {
            println("❌ Error guardando usuarios: ${e.message}")
        }
    }

    // ✅ VERIFICAR LOGIN
    suspend fun authenticate(email: String, password: String): User? {
        return try {
            val users = getAllUsers()
            val userEntry = users[email.lowercase()]

            if (userEntry != null && userEntry.first == password) {
                println("✅ Login fake exitoso: ${userEntry.second.name}")
                userEntry.second
            } else {
                println("❌ Credenciales fake incorrectas para: $email")
                null
            }
        } catch (e: Exception) {
            println("❌ Error en authenticate: ${e.message}")
            null
        }
    }

    // ✅ AGREGAR USUARIO NUEVO
    suspend fun addUser(email: String, password: String, user: User): Boolean {
        return try {
            val currentUsers = getAllUsers().toMutableMap()

            if (!currentUsers.containsKey(email.lowercase())) {
                // Convertir a nueva estructura para guardar
                val newUsersStructure = currentUsers.mapValues { (_, pair) ->
                    FakeUserEntry(pair.first, pair.second)
                }.toMutableMap()

                newUsersStructure[email.lowercase()] = FakeUserEntry(password, user)
                saveUsers(newUsersStructure)
                println("✅ Usuario agregado: ${user.name} (${email})")
                true
            } else {
                println("⚠️ Usuario ya existe: $email")
                false
            }
        } catch (e: Exception) {
            println("❌ Error agregando usuario: ${e.message}")
            false
        }
    }

    // ✅ MODIFICAR USUARIO EXISTENTE
    suspend fun updateUser(email: String, newPassword: String? = null, newUser: User? = null): Boolean {
        return try {
            val currentUsers = getAllUsers().toMutableMap()
            val existingEntry = currentUsers[email.lowercase()]

            if (existingEntry != null) {
                val updatedPassword = newPassword ?: existingEntry.first
                val updatedUser = newUser ?: existingEntry.second

                // Convertir a nueva estructura para guardar
                val newUsersStructure = currentUsers.mapValues { (_, pair) ->
                    FakeUserEntry(pair.first, pair.second)
                }.toMutableMap()

                newUsersStructure[email.lowercase()] = FakeUserEntry(updatedPassword, updatedUser)
                saveUsers(newUsersStructure)
                println("✅ Usuario actualizado: ${updatedUser.name} (${email})")
                true
            } else {
                println("⚠️ Usuario no existe: $email")
                false
            }
        } catch (e: Exception) {
            println("❌ Error actualizando usuario: ${e.message}")
            false
        }
    }

    // ✅ ELIMINAR USUARIO
    suspend fun deleteUser(email: String): Boolean {
        return try {
            val currentUsers = getAllUsers().toMutableMap()

            if (currentUsers.containsKey(email.lowercase())) {
                currentUsers.remove(email.lowercase())

                // Convertir a nueva estructura para guardar
                val newUsersStructure = currentUsers.mapValues { (_, pair) ->
                    FakeUserEntry(pair.first, pair.second)
                }

                saveUsers(newUsersStructure)
                println("✅ Usuario eliminado: $email")
                true
            } else {
                println("⚠️ Usuario no existe para eliminar: $email")
                false
            }
        } catch (e: Exception) {
            println("❌ Error eliminando usuario: ${e.message}")
            false
        }
    }

    // ✅ RESETEAR A USUARIOS POR DEFECTO
    suspend fun resetToDefaults() {
        try {
            saveUsers(defaultUsers)
            println("✅ Usuarios reseteados a valores por defecto")
        } catch (e: Exception) {
            println("❌ Error reseteando usuarios: ${e.message}")
        }
    }

    // ✅ OBTENER USUARIO POR EMAIL
    suspend fun getUserByEmail(email: String): User? {
        return try {
            val users = getAllUsers()
            users[email.lowercase()]?.second
        } catch (e: Exception) {
            println("❌ Error obteniendo usuario por email: ${e.message}")
            null
        }
    }

    // ✅ CAMBIAR CONTRASEÑA
    suspend fun changePassword(email: String, oldPassword: String, newPassword: String): Boolean {
        return try {
            val currentUsers = getAllUsers().toMutableMap()
            val userEntry = currentUsers[email.lowercase()]

            if (userEntry != null && userEntry.first == oldPassword) {
                // Convertir a nueva estructura para guardar
                val newUsersStructure = currentUsers.mapValues { (_, pair) ->
                    FakeUserEntry(pair.first, pair.second)
                }.toMutableMap()

                newUsersStructure[email.lowercase()] = FakeUserEntry(newPassword, userEntry.second)
                saveUsers(newUsersStructure)
                println("✅ Contraseña cambiada para: $email")
                true
            } else {
                println("❌ Credenciales incorrectas para cambio de contraseña: $email")
                false
            }
        } catch (e: Exception) {
            println("❌ Error cambiando contraseña: ${e.message}")
            false
        }
    }
}