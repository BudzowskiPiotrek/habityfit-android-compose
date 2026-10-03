package com.piodev.habityfit.features.informe.data.repository

import com.piodev.database.dao.InformeDao
import com.piodev.habityfit.features.informe.domain.model.InformePeriod
import com.piodev.habityfit.features.informe.domain.model.InformeSummary
import com.piodev.habityfit.features.informe.domain.model.MuscleGroupStat
import com.piodev.habityfit.features.informe.domain.repository.InformeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.Calendar
import javax.inject.Inject

class InformeRepositoryImpl @Inject constructor(
    private val dao: InformeDao
) : InformeRepository {

    override fun getInforme(userId: String, period: InformePeriod): Flow<InformeSummary> {
        val startDate = calculateStartDate(period)

        return combine(
            dao.getSessionStats(userId, startDate),
            dao.getSetStats(userId, startDate),
            dao.getMuscleGroupStats(userId, startDate)
        ) { sessionStats, setStats, muscleGroups ->
            InformeSummary(
                totalWorkouts = sessionStats.totalWorkouts,
                totalDurationMinutes = sessionStats.totalDuration,
                totalVolume = setStats.totalVolume,
                totalSets = setStats.totalSets,
                muscleGroupStats = muscleGroups.map {
                    MuscleGroupStat(
                        muscleGroupId = it.muscleGroupId,
                        muscleGroupName = it.muscleGroupName,
                        totalVolume = it.totalVolume,
                        totalSets = it.totalSets,
                        minSets = it.minSets,
                        maxSets = it.maxSets
                    )
                }
            )
        }
    }

    private fun calculateStartDate(period: InformePeriod): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        when (period) {
            InformePeriod.WEEK -> {
//                calendar.firstDayOfWeek = Calendar.MONDAY
//                val currentDay = calendar.get(Calendar.DAY_OF_WEEK)
//                val daysFromMonday = (currentDay - Calendar.MONDAY + 7) % 7
//                calendar.add(Calendar.DAY_OF_MONTH, -daysFromMonday)
                calendar.add(Calendar.DAY_OF_MONTH, -6)
            }

            InformePeriod.MONTH -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
            }

            InformePeriod.LAST_3_MONTHS -> {
                calendar.add(Calendar.MONTH, -2)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
            }

            InformePeriod.YEAR -> {
                calendar.set(Calendar.MONTH, Calendar.JANUARY)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
            }
        }
        return calendar.timeInMillis
    }
}