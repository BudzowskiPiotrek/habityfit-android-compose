package com.piodev.habityfit.features.community.data.repository

import com.piodev.database.dao.RoutineRequestDao
import com.piodev.database.model.RequestStatus
import com.piodev.habityfit.features.community.data.mapper.toDomain
import com.piodev.habityfit.features.community.domain.model.RoutineRequest
import com.piodev.habityfit.features.community.domain.repository.RoutineRequestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoutineRequestRepositoryImpl @Inject constructor(
    private val dao: RoutineRequestDao
) : RoutineRequestRepository {

    override fun getReceivedRequests(userId: String): Flow<List<RoutineRequest>> {
        return dao.getPendingReceivedWithDetails(userId)
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun acceptRequest(requestId: String): Result<Unit> {
        return try {
            dao.updateRequestStatus(
                requestId,
                RequestStatus.ACCEPTED,
                System.currentTimeMillis()
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun declineRequest(requestId: String): Result<Unit> {
        return try {
            dao.updateRequestStatus(
                requestId,
                RequestStatus.REJECTED,
                System.currentTimeMillis()
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}