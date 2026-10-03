package com.piodev.database.dao

import androidx.room.*
import com.piodev.database.entities.ExerciseSetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseSetDao : BaseDao<ExerciseSetEntity> {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("SELECT * FROM EXERCISE_SETS WHERE WORKOUT_EXERCISE_ID = :workoutExerciseId ORDER BY SET_NUMBER ASC")
    fun getSetsByWorkoutExercise(workoutExerciseId: String): Flow<List<ExerciseSetEntity>>

    @Query("SELECT * FROM EXERCISE_SETS WHERE SYNC_STATUS = 'PENDING'")
    suspend fun getPendingSync(): List<ExerciseSetEntity>

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // SINCRONIZACIÓN
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Query("UPDATE EXERCISE_SETS SET SYNC_STATUS = 'SYNCED' WHERE ID IN (:ids)")
    suspend fun markAsSynced(ids: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSets(sets: List<ExerciseSetEntity>)

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESCRITURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Update
    suspend fun updateSet(set: ExerciseSetEntity)
}