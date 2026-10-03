package com.piodev.network.api

import com.piodev.network.dto.request.*
import com.piodev.network.dto.response.ApiResponse
import com.piodev.network.dto.response.LoginResponseDto
import com.piodev.network.dto.response.SyncErrorDetailDto
import com.piodev.network.dto.sync.SyncPayloadDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FitnessApiService {

    @POST("api/sync/push")
    suspend fun uploadSyncData(
        @Body payload: SyncPayloadDto
    ): Response<ApiResponse<SyncErrorDetailDto>>

    @POST("api/sync/pull")
    suspend fun downloadSyncData(
        @Body request: SyncRequestDto
    ): Response<ApiResponse<SyncPayloadDto>>

    @POST("api/user/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<ApiResponse<LoginResponseDto>>

    @POST("api/invitations/accept")
    suspend fun accept(
        @Body request: AcceptInvitationDto
    ): Response<ApiResponse<LoginResponseDto>>

    @POST("api/invitations/register")
    suspend fun register(
        @Body request: EmailDto
    ): Response<ApiResponse<Any?>>
}