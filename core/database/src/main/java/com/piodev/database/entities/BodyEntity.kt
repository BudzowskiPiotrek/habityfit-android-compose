package com.piodev.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.piodev.database.model.SyncStatus

@Entity(
    tableName = "BODY_MEASUREMENTS",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["ID"],
            childColumns = ["ID_USER"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [Index(value = ["ID_USER"])]
)
data class BodyEntity(

    @PrimaryKey
    @ColumnInfo(name = "ID") override var ID: String,

    @ColumnInfo(name = "CREATED_AT") override var CREATED_AT: Long,

    @ColumnInfo(name = "UPDATED_AT") override var UPDATED_AT: Long?,

    @ColumnInfo(name = "DELETED_AT") override var DELETED_AT: Long?,

    @ColumnInfo(name = "SYNC_STATUS") override var SYNC_STATUS: SyncStatus,

    @ColumnInfo(name = "ID_USER") val idUser: String,

    @ColumnInfo(name = "DATE") val date: Long,

    @ColumnInfo(name = "WEIGHT") val weight: Double,

    @ColumnInfo(name = "MUSCLE_MASS") val muscleMass: Double?,

    @ColumnInfo(name = "FAT_MASS") val fatMass: Double?,

    @ColumnInfo(name = "BODY_WATER") val bodyWater: Double?,

) : BaseEntity(
    ID = ID,
    CREATED_AT = CREATED_AT,
    UPDATED_AT = UPDATED_AT,
    DELETED_AT = DELETED_AT,
    SYNC_STATUS = SYNC_STATUS
)