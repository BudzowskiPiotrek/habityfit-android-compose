package com.piodev.database.dao

import androidx.room.*
import com.piodev.database.entities.ExerciseEntity
import com.piodev.database.entities.WorkoutSessionEntity
import com.piodev.database.model.SyncStatus
import com.piodev.database.model.WorkoutSessionWithExercises
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao : BaseDao<WorkoutSessionEntity> {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM WORKOUT_SESSIONS WHERE ID = :id LIMIT 1")
    suspend fun getWorkoutById(id: String): WorkoutSessionEntity?

    @Transaction
    @Query("SELECT * FROM WORKOUT_SESSIONS WHERE USER_ID = :userId AND DELETED_AT IS NULL ORDER BY DATE DESC")
    fun getWorkoutHistory(userId: String): Flow<List<WorkoutSessionWithExercises>>

    @Transaction
    @Query("SELECT * FROM WORKOUT_SESSIONS WHERE USER_ID = :userId AND DELETED_AT IS NULL ORDER BY DATE ASC")
    fun getWorkoutHistoryAsc(userId: String): Flow<List<WorkoutSessionWithExercises>>

    @Transaction
    @Query("SELECT * FROM WORKOUT_SESSIONS WHERE ID = :sessionId")
    fun getSessionByIdFlow(sessionId: String): Flow<WorkoutSessionWithExercises?>

    @Query("SELECT COUNT(*) FROM WORKOUT_SESSIONS WHERE USER_ID = :userId AND DELETED_AT IS NULL AND DATE >= :start AND DATE <= :end")
    suspend fun countSessionsBetween(userId: String, start: Long, end: Long): Int

    @Query(
        """
    SELECT e.* FROM WORKOUT_EXERCISES we
    JOIN EXERCISE e ON we.EXERCISE_ID = e.ID
    WHERE we.ID = :workoutExerciseId
    LIMIT 1
    """
    )
    suspend fun getExerciseByWorkoutExerciseId(workoutExerciseId: String): ExerciseEntity?

    @Query(
        """
    SELECT we.ID 
    FROM workout_exercises we
    JOIN WORKOUT_SESSIONS ws ON we.SESSION_ID = ws.ID
    WHERE we.EXERCISE_ID = :exerciseId 
    AND we.SORT_ORDER = :order
    AND we.DELETED_AT IS NULL
    ORDER BY ws.DATE DESC
    LIMIT 1
    """
    )
    suspend fun getLastWorkoutExerciseId(exerciseId: String, order: Int): String?

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESCRITURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("UPDATE WORKOUT_SESSIONS SET DATE = :date, DURATION= :duration, NOTES = :notes, UPDATED_AT = :update, SYNC_STATUS = :sync WHERE ID = :id")
    suspend fun updateWorkoutDetails(
        id: String,
        date: Long,
        duration: Int,
        notes: String?,
        sync: SyncStatus,
        update: Long,
    )

    @Query("UPDATE WORKOUT_SESSIONS SET DURATION = :duration, UPDATED_AT = :update, SYNC_STATUS = 'PENDING' WHERE ID = :id")
    suspend fun updateWorkoutDuration(
        id: String,
        duration: Int,
        update: Long,
    )

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BORRAR (SOFT DELETE)
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("UPDATE WORKOUT_SESSIONS SET DELETED_AT = :date, UPDATED_AT = :date, SYNC_STATUS = 'PENDING' WHERE ID = :id")
    suspend fun softDeleteById(id: String, date: Long)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SINCRONIZACIÓN
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM WORKOUT_SESSIONS WHERE SYNC_STATUS = 'PENDING'")
    suspend fun getPendingSync(): List<WorkoutSessionEntity>

    @Query("UPDATE WORKOUT_SESSIONS SET SYNC_STATUS = 'SYNCED' WHERE ID IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)
}