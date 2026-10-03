package com.piodev.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.piodev.database.model.Sex
import com.piodev.database.model.SyncStatus

@Entity(tableName = "USERS")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "ID") override var ID: String,

    @ColumnInfo(name = "CREATED_AT") override var CREATED_AT: Long,

    @ColumnInfo(name = "UPDATED_AT") override var UPDATED_AT: Long?,

    @ColumnInfo(name = "DELETED_AT") override var DELETED_AT: Long?,

    @ColumnInfo(name = "SYNC_STATUS") override var SYNC_STATUS: SyncStatus,

    @ColumnInfo(name = "EMAIL") val email: String,

    @ColumnInfo(name = "PASSWORD_HASH") val passwordHash: String?,

    @ColumnInfo(name = "NAME") val fullName: String,

    @ColumnInfo(name = "SEX") val sex: Sex?,

    @ColumnInfo(name = "BIRTH_DATE") val birthDate: Long?,

    @ColumnInfo(name = "HEIGHT") val height: Double?,

    @ColumnInfo(name = "LAST_SYNC_DATE") val lastSyncDate: Long? = null,

    @ColumnInfo(name = "TERMS_ACCEPTED_AT") val termsAcceptedAt: Long? = null

) : BaseEntity(
    ID = ID,
    CREATED_AT = CREATED_AT,
    UPDATED_AT = UPDATED_AT,
    DELETED_AT = DELETED_AT,
    SYNC_STATUS = SYNC_STATUS
)