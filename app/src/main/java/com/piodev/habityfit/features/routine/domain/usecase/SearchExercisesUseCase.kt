package com.piodev.habityfit.features.routine.domain.usecase

import com.piodev.habityfit.core.util.normalizeForSearch
import com.piodev.habityfit.features.exercises.domain.model.Exercise
import com.piodev.habityfit.features.exercises.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchExercisesUseCase @Inject constructor(
    private val exerciseRepository: ExerciseRepository
) {

    operator fun invoke(query: String): Flow<List<Exercise>> {
        val queryTokens = query.split(" ")
            .filter { it.isNotEmpty() }
            .map { it.normalizeForSearch() }

        return exerciseRepository.getExercises()
            .map { list ->
                list.filter { exercise ->
                    queryTokens.all { token ->
                        exercise.name.normalizeForSearch().contains(token) ||
                                exercise.muscleGroups.any {
                                    it.name.normalizeForSearch().contains(token)
                                }
                    }
                }
            }
    }
}