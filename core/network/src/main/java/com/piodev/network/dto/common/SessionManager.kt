package com.piodev.network.dto.common

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = createEncryptedPreferences(context)

    companion object {
        private const val ENCRYPTED_PREFS_NAME = "setpoint_auth_prefs_encrypted"
        private const val LEGACY_PREFS_NAME = "setpoint_auth_prefs"
        private const val KEY_JWT_TOKEN = "jwt_token"
        private const val KEY_USER_ID = "logged_user_id"
    }

    init {
        migrateLegacySession()
    }

    fun saveSession(token: String, userId: String) {
        prefs.edit()
            .putString(KEY_JWT_TOKEN, token)
            .putString(KEY_USER_ID, userId)
            .apply()
    }

    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_JWT_TOKEN, token).apply()
    }

    fun clearToken() {
        prefs.edit().remove(KEY_JWT_TOKEN).apply()
    }

    fun getToken(): String? {
        val token = prefs.getString(KEY_JWT_TOKEN, null) ?: return null

        if (isTokenExpired(token)) {
            clearToken()
            return null
        }
        return token
    }

    private fun isTokenExpired(token: String): Boolean {
        try {
            val parts = token.split(".")
            if (parts.size < 2) return true

            // Decodificamos el Payload (segunda parte)
            val payload = String(
                Base64.decode(
                    parts[1],
                    Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
                )
            )
            val json = JSONObject(payload)

            // El campo "exp" es obligatorio en un JWT estándar de servidor
            val expTimestamp = json.getLong("exp")
            val currentTimestamp = System.currentTimeMillis() / 1000
            // Restar 60 segundos  por el tiempo de la llamada
            return currentTimestamp >= (expTimestamp - 60)
        } catch (e: Exception) {
            return true
        }
    }

    private fun migrateLegacySession() {
        val legacy = context.getSharedPreferences(LEGACY_PREFS_NAME, Context.MODE_PRIVATE)
        val legacyToken = legacy.getString(KEY_JWT_TOKEN, null)
        val legacyUserId = legacy.getString(KEY_USER_ID, null)

        if (legacyToken == null && legacyUserId == null) return

        prefs.edit().apply {
            legacyToken?.let { putString(KEY_JWT_TOKEN, it) }
            legacyUserId?.let { putString(KEY_USER_ID, it) }
        }.commit()

        legacy.edit().clear().commit()
    }

    private fun createEncryptedPreferences(context: Context): SharedPreferences {
        return try {
            buildEncryptedPreferences(context)
        } catch (e: Exception) {
            context.deleteSharedPreferences(ENCRYPTED_PREFS_NAME)
            buildEncryptedPreferences(context)
        }
    }

    private fun buildEncryptedPreferences(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}
