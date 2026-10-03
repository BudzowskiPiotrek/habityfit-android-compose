package com.piodev.data.repository

interface SyncRepository {
    suspend fun syncAll(): Result<Unit>
    suspend fun pushLocalChanges(): Result<Unit>
    suspend fun pullRemoteChanges(): Result<Unit>
}