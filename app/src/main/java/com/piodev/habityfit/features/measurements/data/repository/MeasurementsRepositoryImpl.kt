package com.piodev.habityfit.features.measurements.data.repository

import com.piodev.database.dao.BodyDao
import com.piodev.habityfit.features.measurements.data.mapper.toDomain
import com.piodev.habityfit.features.measurements.data.mapper.toEntity
import com.piodev.habityfit.features.measurements.domain.model.Body
import com.piodev.habityfit.features.measurements.domain.repository.MeasurementsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MeasurementsRepositoryImpl @Inject constructor(
    private val dao: BodyDao
) : MeasurementsRepository {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override fun getBodyData(userId: String): Flow<List<Body>> {
        return dao.getHistoryByUserId(userId).map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getBodyById(id: String): Body? {
        return dao.getBodyById(id)?.toDomain()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESCRITURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun saveBodyData(userId: String, body: Body) {
        val bodyEntity = body.toEntity(userId)
        dao.insert(bodyEntity)
    }

    override suspend fun updateBody(body: Body) {

        val existing = dao.getBodyById(body.id)
        if (existing != null) {

            dao.update(body.toEntity(existing.idUser, existing))
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BORRAR
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun deleteBodyData(id: String) {
        dao.softDeleteById(
            id,
            date = System.currentTimeMillis()
        )
    }
}