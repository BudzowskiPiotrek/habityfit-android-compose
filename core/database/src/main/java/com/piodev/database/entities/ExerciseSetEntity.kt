package com.piodev.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.piodev.database.model.SyncStatus

@Entity(
    tableName = "EXERCISE_SETS",
    foreignKeys = [
        ForeignKey(
            entity = WorkoutExerciseEntity::class,
            parentColumns = ["ID"],
            childColumns = ["WORKOUT_EXERCISE_ID"],
            onDelete = ForeignKey.NO_ACTION
        )
    ],
    indices = [Index(value = ["WORKOUT_EXERCISE_ID"])]
)
data class ExerciseSetEntity(

    @PrimaryKey
    @ColumnInfo(name = "ID") override var ID: String,

    @ColumnInfo(name = "CREATED_AT") override var CREATED_AT: Long,

    @ColumnInfo(name = "UPDATED_AT") override var UPDATED_AT: Long?,

    @ColumnInfo(name = "DELETED_AT") override var DELETED_AT: Long?,

    @ColumnInfo(name = "SYNC_STATUS") override var SYNC_STATUS: SyncStatus,

    @ColumnInfo(name = "WORKOUT_EXERCISE_ID") val workoutExerciseId: String,

    @ColumnInfo(name = "SET_NUMBER") val setNumber: Int,

    @ColumnInfo(name = "REPS") val reps: Int,

    @ColumnInfo(name = "WEIGHT") val weight: Double?,

    @ColumnInfo(name = "RPE") val rpe: Double?,

    ) : BaseEntity(
    ID = ID,
    CREATED_AT = CREATED_AT,
    UPDATED_AT = UPDATED_AT,
    DELETED_AT = DELETED_AT,
    SYNC_STATUS = SYNC_STATUS
)