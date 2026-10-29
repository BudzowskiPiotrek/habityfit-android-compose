package com.piodev.data.mapper

import com.piodev.data.model.BodyMeasurement
import com.piodev.data.model.User
import com.piodev.data.model.UserInvitation
import com.piodev.data.model.UserRelation
import com.piodev.database.entities.BodyEntity
import com.piodev.database.entities.UserEntity
import com.piodev.database.entities.UserInvitationEntity
import com.piodev.database.entities.UserRelationEntity
import com.piodev.database.model.InvitationStatus
import com.piodev.database.model.SyncStatus
import com.piodev.network.dto.sync.BodyMeasurementDto
import com.piodev.network.dto.sync.UserDto
import com.piodev.network.dto.sync.UserRelationDto
import com.piodev.network.dto.sync.UserInvitationDto
import java.util.UUID
import java.util.Date

// --- USER ---

fun UserDto.toDomain() = User(
    id = this.id,
    email = this.email,
    fullName = this.fullName,
    sex = this.sex,
    birthDate = this.birthDate?.time,
    height = this.height,
    termsAcceptedAt = this.termsAcceptedAt?.time

)

fun UserEntity.toDomain() = User(
    id = this.ID,
    email = this.email,
    fullName = this.fullName,
    sex = this.sex,
    birthDate = this.birthDate,
    height = this.height,
    termsAcceptedAt = this.termsAcceptedAt
)

fun User.toEntity(passwordHash: String = ""): UserEntity = UserEntity(
    email = this.email,
    passwordHash = passwordHash,
    fullName = this.fullName,
    sex = this.sex,
    birthDate = this.birthDate,
    height = this.height,
    lastSyncDate = null,
    termsAcceptedAt = this.termsAcceptedAt,
    ID = this.id.ifBlank { UUID.randomUUID().toString() },
    CREATED_AT = System.currentTimeMillis(),
    UPDATED_AT = null,
    DELETED_AT = null,
    SYNC_STATUS = SyncStatus.PENDING
)

fun UserEntity.toDto() = UserDto(
    id = this.ID,
    createdAt = Date(this.CREATED_AT),
    updatedAt = this.UPDATED_AT?.let { Date(it) },
    deletedAt = this.DELETED_AT?.let { Date(it) },
    email = this.email,
    fullName = this.fullName,
    sex = this.sex,
    birthDate = this.birthDate?.let { Date(it) },
    height = this.height,
    termsAcceptedAt = this.termsAcceptedAt?.let { Date(it) }
)

fun UserDto.toEntity(passwordHash: String = ""): UserEntity = UserEntity(
    email = this.email,
    passwordHash = passwordHash,
    fullName = this.fullName,
    sex = this.sex,
    birthDate = this.birthDate?.time,
    height = this.height,
    lastSyncDate = System.currentTimeMillis(),
    termsAcceptedAt = this.termsAcceptedAt?.time,
    ID = this.id,
    CREATED_AT = this.createdAt.time,
    UPDATED_AT = this.updatedAt?.time,
    DELETED_AT = this.deletedAt?.time,
    SYNC_STATUS = SyncStatus.SYNCED
)

// --- USER RELATIONS ---

fun UserRelationDto.toDomain() = UserRelation(
    id = this.id,
    userId = this.userId,
    friendId = this.friendId,
    status = this.status
)

fun UserRelationEntity.toDomain() = UserRelation(
    id = this.ID,
    userId = this.userId,
    friendId = this.friendId,
    status = this.status
)

fun UserRelation.toEntity(): UserRelationEntity = UserRelationEntity(
    userId = this.userId,
    friendId = this.friendId,
    status = this.status,
    ID = this.id.ifBlank { UUID.randomUUID().toString() },
    CREATED_AT = System.currentTimeMillis(),
    UPDATED_AT = null,
    DELETED_AT = null,
    SYNC_STATUS = SyncStatus.PENDING
)

fun UserRelationEntity.toDto() = UserRelationDto(
    id = this.ID,
    createdAt = Date(this.CREATED_AT),
    updatedAt = this.UPDATED_AT?.let { Date(it) },
    deletedAt = this.DELETED_AT?.let { Date(it) },
    userId = this.userId,
    friendId = this.friendId,
    status = this.status
)

fun UserRelationDto.toEntity(): UserRelationEntity = UserRelationEntity(
    userId = this.userId,
    friendId = this.friendId,
    status = this.status,
    ID = this.id,
    CREATED_AT = this.createdAt.time,
    UPDATED_AT = this.updatedAt?.time,
    DELETED_AT = this.deletedAt?.time,
    SYNC_STATUS = SyncStatus.SYNCED
)

// --- USER INVITATIONS ---

fun UserInvitationDto.toDomain() = UserInvitation(
    id = this.id.toString(),
    email = this.email
)

fun UserInvitationEntity.toDomain() = UserInvitation(
    id = this.ID,
    email = this.email,
)

fun UserInvitationEntity.toDto() = UserInvitationDto(
    id = this.ID,
    email = this.email,
)

fun UserInvitation.toEntity(): UserInvitationEntity = UserInvitationEntity(
    email = this.email,
    senderUserId =  UUID.randomUUID().toString() ,
    ID = this.id.ifBlank { UUID.randomUUID().toString() },
    CREATED_AT = System.currentTimeMillis(),
    UPDATED_AT = null,
    DELETED_AT = null,
    SYNC_STATUS = SyncStatus.PENDING,
    token = null,
    expiresAt = null,
    status = InvitationStatus.PENDING,
    sended = false
)

fun UserInvitationDto.toEntity(): UserInvitationEntity = UserInvitationEntity(
    email = this.email,
    senderUserId =  UUID.randomUUID().toString() ,
    ID = this.id,
    CREATED_AT = System.currentTimeMillis(),
    UPDATED_AT = null,
    DELETED_AT = null,
    SYNC_STATUS = SyncStatus.SYNCED,
    token = null,
    expiresAt = null,
    status = InvitationStatus.PENDING,
    sended = true
)

// --- BODY MEASUREMENTS ---

fun BodyMeasurementDto.toDomain() = BodyMeasurement(
    id = this.id,
    idUser = this.idUser,
    date = this.date.time,
    weight = this.weight,
    muscleMass = this.muscleMass,
    fatMass = this.fatMass,
    bodyWater = this.bodyWater
)

fun BodyEntity.toDomain() = BodyMeasurement(
    id = this.ID,
    idUser = this.idUser,
    date = this.date,
    weight = this.weight,
    muscleMass = this.muscleMass,
    fatMass = this.fatMass,
    bodyWater = this.bodyWater
)

fun BodyMeasurement.toEntity(): BodyEntity {
    val entity = BodyEntity(
        idUser = this.idUser,
        date = this.date,
        weight = this.weight,
        muscleMass = this.muscleMass,
        fatMass = this.fatMass,
        bodyWater = this.bodyWater,
        ID = this.id,
        CREATED_AT = System.currentTimeMillis(),
        UPDATED_AT = null,
        DELETED_AT = null,
        SYNC_STATUS = SyncStatus.PENDING
    )
    return entity
}

fun BodyEntity.toDto() = BodyMeasurementDto(
    id = this.ID,
    createdAt = Date(this.CREATED_AT),
    updatedAt = this.UPDATED_AT?.let { Date(it) },
    deletedAt = this.DELETED_AT?.let { Date(it) },
    idUser = this.idUser,
    date = Date(this.date),
    weight = this.weight,
    muscleMass = this.muscleMass,
    fatMass = this.fatMass,
    bodyWater = this.bodyWater
)

fun BodyMeasurementDto.toEntity(): BodyEntity {
    val entity = BodyEntity(
        idUser = this.idUser,
        date = this.date.time,
        weight = this.weight,
        muscleMass = this.muscleMass,
        fatMass = this.fatMass,
        bodyWater = this.bodyWater,
        ID = this.id,
        CREATED_AT = this.createdAt.time,
        UPDATED_AT = this.updatedAt?.time,
        DELETED_AT = this.deletedAt?.time,
        SYNC_STATUS = SyncStatus.SYNCED
    )
    return entity
}
