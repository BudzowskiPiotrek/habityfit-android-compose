package com.piodev.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.piodev.database.model.SyncStatus

@Entity(
    tableName = "WORKOUT_EXERCISES",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutSessionEntity::class,
            parentColumns = ["ID"],
            childColumns = ["SESSION_ID"],
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = ExerciseEntity::class,
            parentColumns = ["ID"],
            childColumns = ["EXERCISE_ID"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [
        Index(value = ["SESSION_ID"]),
        Index(value = ["EXERCISE_ID"])
    ]
)
data class WorkoutExerciseEntity(

    @PrimaryKey
    @ColumnInfo(name = "ID") override var ID: String,

    @ColumnInfo(name = "CREATED_AT") override var CREATED_AT: Long,

    @ColumnInfo(name = "UPDATED_AT") override var UPDATED_AT: Long?,

    @ColumnInfo(name = "DELETED_AT") override var DELETED_AT: Long?,

    @ColumnInfo(name = "SYNC_STATUS") override var SYNC_STATUS: SyncStatus,

    @ColumnInfo(name = "SESSION_ID") val sessionId: String,

    @ColumnInfo(name = "EXERCISE_ID") val exerciseId: String,

    @ColumnInfo(name = "SORT_ORDER") val order: Int,

    @ColumnInfo(name = "REST_SECOND") val restSecond: Int,

) : BaseEntity(
    ID = ID,
    CREATED_AT = CREATED_AT,
    UPDATED_AT = UPDATED_AT,
    DELETED_AT = DELETED_AT,
    SYNC_STATUS = SYNC_STATUS
)