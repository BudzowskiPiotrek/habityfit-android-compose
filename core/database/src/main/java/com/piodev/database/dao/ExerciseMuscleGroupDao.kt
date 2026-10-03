package com.piodev.database.dao

import androidx.room.*
import com.piodev.database.entities.ExerciseMuscleGroupEntity

@Dao
interface ExerciseMuscleGroupDao : BaseDao<ExerciseMuscleGroupEntity> {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT MUSCLE_GROUP_ID FROM EXERCISE_MUSCLE_GROUP WHERE EXERCISE_ID = :exerciseId")
    suspend fun getMuscleGroupsByExercise(exerciseId: String): List<String>

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SINCRONIZACIÓN
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM EXERCISE_MUSCLE_GROUP WHERE SYNC_STATUS = 'PENDING'")
    suspend fun getPendingSync(): List<ExerciseMuscleGroupEntity>

    @Query("UPDATE EXERCISE_MUSCLE_GROUP SET SYNC_STATUS = 'SYNCED' WHERE ID IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)
}