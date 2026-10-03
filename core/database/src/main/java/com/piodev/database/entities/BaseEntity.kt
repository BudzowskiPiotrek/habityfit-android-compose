package com.piodev.database.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.piodev.database.model.SyncStatus

abstract class BaseEntity(
    @PrimaryKey
    @ColumnInfo(name = "ID")
    open var ID: String,

    @ColumnInfo(name = "CREATED_AT")
    open var CREATED_AT: Long,

    @ColumnInfo(name = "UPDATED_AT")
    open var UPDATED_AT: Long?,

    @ColumnInfo(name = "DELETED_AT")
    open var DELETED_AT: Long?,

    @ColumnInfo(name = "SYNC_STATUS")
    open var SYNC_STATUS: SyncStatus
)