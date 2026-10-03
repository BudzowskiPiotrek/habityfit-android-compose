package com.piodev.habityfit.features.measurements.data.mapper

import com.piodev.database.entities.BodyEntity
import com.piodev.database.model.SyncStatus
import com.piodev.habityfit.features.measurements.domain.model.Body
import java.util.UUID

fun BodyEntity.toDomain(): Body {
    return Body(
        id = this.ID,
        date = this.date,
        weight = this.weight,
        muscleMass = this.muscleMass,
        fatMass = this.fatMass,
        bodyWater = this.bodyWater
    )
}

fun Body.toEntity(
    userId: String,
    existingEntity: BodyEntity? = null
): BodyEntity {
    return BodyEntity(
        ID = this.id.ifBlank { existingEntity?.ID ?: UUID.randomUUID().toString() },
        CREATED_AT = existingEntity?.CREATED_AT ?: System.currentTimeMillis(),
        UPDATED_AT = System.currentTimeMillis(),
        DELETED_AT = null,
        SYNC_STATUS = SyncStatus.PENDING,
        idUser = userId,
        date = this.date,
        weight = this.weight,
        muscleMass = this.muscleMass,
        fatMass = this.fatMass,
        bodyWater = this.bodyWater
    )
}