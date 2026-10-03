package com.piodev.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.piodev.database.model.InformeSessionStatsRaw
import com.piodev.database.model.InformeSetStatsRaw
import com.piodev.database.model.MuscleGroupStatRaw
import kotlinx.coroutines.flow.Flow

@Dao
interface InformeDao {

    @Query("""
        SELECT COUNT(*) as totalWorkouts, COALESCE(SUM(DURATION), 0) as totalDuration
        FROM WORKOUT_SESSIONS
        WHERE USER_ID = :userId AND DELETED_AT IS NULL AND DATE >= :startDate
    """)
    fun getSessionStats(userId: String, startDate: Long): Flow<InformeSessionStatsRaw>

    @Query("""
        SELECT COALESCE(SUM(es.WEIGHT * es.REPS), 0.0) as totalVolume, COUNT(es.ID) as totalSets
        FROM EXERCISE_SETS es
        INNER JOIN WORKOUT_EXERCISES we ON we.ID = es.WORKOUT_EXERCISE_ID
        INNER JOIN WORKOUT_SESSIONS ws ON ws.ID = we.SESSION_ID
        WHERE ws.USER_ID = :userId AND ws.DELETED_AT IS NULL AND ws.DATE >= :startDate AND es.DELETED_AT IS NULL
    """)
    fun getSetStats(userId: String, startDate: Long): Flow<InformeSetStatsRaw>

    @Query("""
        SELECT mg.ID as muscleGroupId, mg.NAME as muscleGroupName,
               COALESCE(SUM(es.WEIGHT * es.REPS), 0.0) as totalVolume,
               COALESCE(COUNT(es.ID), 0) as totalSets,
               mg.MIN_SETS as minSets,
               mg.MAX_SETS as maxSets
        FROM MUSCLE_GROUP mg
        LEFT JOIN EXERCISE_MUSCLE_GROUP emg ON emg.MUSCLE_GROUP_ID = mg.ID
        LEFT JOIN (
            SELECT we.ID as weId, we.EXERCISE_ID as exerciseId
            FROM WORKOUT_EXERCISES we
            INNER JOIN WORKOUT_SESSIONS ws ON ws.ID = we.SESSION_ID
            WHERE ws.USER_ID = :userId AND ws.DELETED_AT IS NULL AND ws.DATE >= :startDate
        ) filteredWe ON filteredWe.exerciseId = emg.EXERCISE_ID
        LEFT JOIN EXERCISE_SETS es ON es.WORKOUT_EXERCISE_ID = filteredWe.weId AND es.DELETED_AT IS NULL
        GROUP BY mg.ID, mg.NAME, mg.MIN_SETS, mg.MAX_SETS
        ORDER BY mg.NAME ASC
    """)
    fun getMuscleGroupStats(userId: String, startDate: Long): Flow<List<MuscleGroupStatRaw>>
}