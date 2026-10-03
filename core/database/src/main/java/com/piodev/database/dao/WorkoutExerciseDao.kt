package com.piodev.database.dao

import androidx.room.*
import com.piodev.database.entities.WorkoutExerciseEntity

@Dao
interface WorkoutExerciseDao : BaseDao<WorkoutExerciseEntity> {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM WORKOUT_EXERCISES WHERE SYNC_STATUS = 'PENDING'")
    suspend fun getPendingSync(): List<WorkoutExerciseEntity>

    @Query("SELECT REST_SECOND FROM WORKOUT_EXERCISES WHERE ID = :workoutExerciseId")
    suspend fun getRestSeconds(workoutExerciseId: String): Int?

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SINCRONIZACIÓN
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("UPDATE WORKOUT_EXERCISES SET SYNC_STATUS = 'SYNCED' WHERE ID IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutExercises(exercises: List<WorkoutExerciseEntity>)
}