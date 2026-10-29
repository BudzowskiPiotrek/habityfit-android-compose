package com.piodev.habityfit.features.auth.data.repository

import com.piodev.data.mapper.toEntity
import com.piodev.data.repository.SyncRepository
import com.piodev.database.AppDatabase
import com.piodev.database.dao.UserDao
import com.piodev.database.model.SyncStatus
import com.piodev.network.api.FitnessApiService
import com.piodev.network.dto.common.SessionManager
import com.piodev.network.dto.request.AcceptInvitationDto
import com.piodev.network.dto.request.EmailDto
import com.piodev.network.dto.request.LoginRequestDto
import com.piodev.network.dto.response.ApiResponse
import com.piodev.network.dto.response.LoginResponseDto
import com.piodev.habityfit.core.config.LimitsConfig
import com.piodev.habityfit.features.auth.data.mapper.toDomain
import com.piodev.habityfit.features.auth.domain.model.User
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: FitnessApiService,
    private val sessionManager: SessionManager,
    private val userDao: UserDao,
    private val sync: SyncRepository,
    private val db: AppDatabase
) : AuthRepository {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ACCEPT, REGISTRO, LOGIN Y LOGOUT
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun accept(token: String, fullName: String, password: String): Result<User> {
        return try {
            val response = authApiService.accept(AcceptInvitationDto(token, fullName, password))
            handleAuthResponse(response)
        } catch (e: Exception) {
            Result.failure(Exception("Servidor no disponible, o token de invitación ha expirado"))
        }
    }

    override suspend fun register(email: String): Result<Unit> {
        return try {
            val response =
                authApiService.register(EmailDto(email = email))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(mapRegisterError(response.code())))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Servicio no disponible, prueba más tarde"))
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val response = authApiService.login(LoginRequestDto(email, password))
            handleAuthResponse(response)
        } catch (e: Exception) {
            Result.failure(Exception("Servidor no disponible, prueba más tarde"))
        }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            try {
                sync.pushLocalChanges()
            } catch (_: Exception) {
            }
            db.clearAllTables()
        }
        sessionManager.clear()
    }

    override suspend fun acceptTerms(userId: String): Result<Unit> {
        return try {
            val timestamp = System.currentTimeMillis()
            val existing = userDao.getUserById(userId)
                ?: return Result.failure(Exception("Usuario no encontrado"))
            val updated = existing.copy(
                termsAcceptedAt = timestamp,
                UPDATED_AT = timestamp,
                SYNC_STATUS = SyncStatus.PENDING
            )
            userDao.update(updated)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // OBTENER USUARIO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun getCurrentUser(): Flow<User?> {
        val userId = sessionManager.getUserId()
        return if (userId == null) {
            flowOf(null)
        } else {
            userDao.getUserByIdFlow(userId).map { it?.toDomain() }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // METODOS COMUNES
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private suspend fun handleAuthResponse(response: Response<ApiResponse<LoginResponseDto>>): Result<User> {
        if (!response.isSuccessful)
            return Result.failure(Exception(mapAuthError(response.code())))

        val apiResponse = response.body()
            ?: return Result.failure(Exception("Servicio no disponible, prueba más tarde"))

        val loginData = apiResponse.result
            ?: return Result.failure(Exception("Servicio no disponible, prueba más tarde"))

        sessionManager.saveSession(loginData.token, loginData.user.id)

        val userEntity = loginData.user.toEntity().copy(lastSyncDate = 946684800000L)
        // 01/01/2000 00:00:00 UTC
        userDao.insert(userEntity)

        return Result.success(loginData.toDomain())
    }

    private fun mapRegisterError(code: Int): String = when (code) {
        400 -> "Formato de email incorrecto"
        409 -> "Este correo ya tiene cuenta"
        else -> "Servicio no disponible, prueba más tarde"
    }

    private fun mapAuthError(code: Int): String = when (code) {
        500 -> "Fallo de credenciales"
        else -> "Servidor no disponible, prueba más tarde"
    }
}