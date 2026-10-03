package com.piodev.database.dao

import androidx.room.*
import com.piodev.database.entities.ExerciseEntity
import com.piodev.database.model.ExerciseWithMuscleGroups
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao : BaseDao<ExerciseEntity> {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Transaction
    @Query("SELECT * FROM EXERCISE ORDER BY NAME ASC")
    fun getExercisesWithMuscleGroups(): Flow<List<ExerciseWithMuscleGroups>>

    @Query("SELECT * FROM EXERCISE WHERE ID = :exerciseId")
    suspend fun getExerciseById(exerciseId: String): ExerciseEntity?

    @Transaction
    @Query(
        """
        SELECT EXERCISE.* FROM EXERCISE 
        INNER JOIN EXERCISE_MUSCLE_GROUP ON EXERCISE.ID = EXERCISE_MUSCLE_GROUP.EXERCISE_ID
        WHERE EXERCISE_MUSCLE_GROUP.MUSCLE_GROUP_ID = :muscleGroupId
        ORDER BY name ASC
    """
    )
    fun getExercisesByMuscleGroup(muscleGroupId: String): Flow<List<ExerciseWithMuscleGroups>>

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SINCRONIZACIÓN
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM EXERCISE WHERE SYNC_STATUS = 'PENDING'")
    suspend fun getPendingSync(): List<ExerciseEntity>

    @Query("UPDATE EXERCISE SET SYNC_STATUS = 'SYNCED' WHERE ID IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)
}