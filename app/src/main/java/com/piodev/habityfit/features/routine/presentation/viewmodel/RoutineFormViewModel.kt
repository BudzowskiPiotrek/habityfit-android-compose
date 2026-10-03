package com.piodev.habityfit.features.routine.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piodev.habityfit.features.exercises.domain.repository.ExerciseRepository
import com.piodev.habityfit.features.routine.domain.model.Routine
import com.piodev.habityfit.features.routine.domain.model.RoutineExercise
import com.piodev.habityfit.features.routine.domain.repository.RoutineRepository
import com.piodev.habityfit.features.routine.presentation.state.RoutineEditorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.piodev.habityfit.core.config.LimitsConfig.MAX_EXERCISE_WEIGHT
import com.piodev.habityfit.core.config.LimitsConfig.MAX_REPS
import com.piodev.habityfit.core.config.LimitsConfig.MAX_REST_SECONDS
import com.piodev.habityfit.core.config.LimitsConfig.MAX_ROUTINE_DESC
import com.piodev.habityfit.core.config.LimitsConfig.MAX_ROUTINE_NAME
import com.piodev.habityfit.core.config.LimitsConfig.MAX_SETS
import com.piodev.habityfit.core.util.limitTextLength
import com.piodev.habityfit.core.util.normalizeDouble
import com.piodev.habityfit.core.util.normalizeForSearch
import com.piodev.habityfit.core.util.normalizeInt
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.exercises.domain.model.Exercise
import com.piodev.habityfit.features.routine.presentation.state.ExerciseConfigForm
import kotlinx.coroutines.flow.first


@HiltViewModel
class RoutineFormViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // INIT ESTADO Y EVENTOS DE FORMULARIO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    var state by mutableStateOf(RoutineEditorState())
        private set

    init {
        searchExercises("")
    }

    fun onNameChange(newName: String) {
        state = state.copy(name = limitTextLength(newName, maxLength = MAX_ROUTINE_NAME))
    }

    fun onDescriptionChange(newDesc: String) {
        state = state.copy(description = limitTextLength(newDesc, maxLength = MAX_ROUTINE_DESC))
    }

    fun onExerciseSelectedForConfig(exercise: Exercise) {
        state = state.copy(selectedExerciseForConfig = exercise)
    }

    fun onSearchQueryChange(query: String) {
        state = state.copy(searchQuery = query)
        searchExercises(query)
    }

    fun onConfigFormChange(newForm: ExerciseConfigForm) {
        val normalizedForm = newForm.copy(
            sets = normalizeInt(input = newForm.sets, max = MAX_SETS),
            reps = normalizeInt(input = newForm.reps, max = MAX_REPS),
            rest = normalizeInt(input = newForm.rest, max = MAX_REST_SECONDS),
            weight = normalizeDouble(input = newForm.weight, max = MAX_EXERCISE_WEIGHT)
        )
        state = state.copy(configForm = normalizedForm)
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE BUSCA DE EXERCÍCIOS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun searchExercises(query: String) {
        viewModelScope.launch {
            val queryTokens = query.split(" ")
                .filter { it.isNotEmpty() }
                .map { it.normalizeForSearch() }

            state = state.copy(isSearching = true)

            exerciseRepository.getExercises().collect { list ->
                val filtered = list.filter { exercise ->
                    queryTokens.all { token ->
                        exercise.name.normalizeForSearch().contains(token) ||
                                exercise.muscleGroups.any { muscle ->
                                    muscle.name.normalizeForSearch().contains(token)
                                }
                    }
                }
                state = state.copy(
                    searchResults = filtered,
                    isSearching = false
                )
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE CONFIGURACION DE EXERCICIO
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun confirmExercise(sets: Int, reps: Int, restSeconds: Int, targetWeight: Double?) {
        state.selectedExerciseForConfig?.let { exercise ->
            val newList = state.selectedExercises + RoutineExercise(
                id = "",
                exerciseId = exercise.id,
                name = exercise.name,
                order = state.selectedExercises.size,
                sets = sets,
                reps = reps,
                restSecond = restSeconds,
                targetWeight = targetWeight
            )
            state = state.copy(
                selectedExercises = newList,
                selectedExerciseForConfig = null,
                searchQuery = "",
                configForm = ExerciseConfigForm()
            )
        }
    }

    fun cancelConfig() {
        state = state.copy(
            selectedExerciseForConfig = null,
            searchQuery = "",
            configForm = ExerciseConfigForm()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE GUARDADO, ELIMINACIÓN Y REORDENAMIENTO DE EXERCÍCIOS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun saveRoutine() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                val currentId = state.routineId ?: ""
                val routine = Routine(
                    id = currentId,
                    name = state.name,
                    description = state.description
                )
                val userId = authRepository.getCurrentUser().first()?.id ?: ""
                if (state.routineId.isNullOrBlank()) {
                    routineRepository.saveRoutineWithExercises(
                        routine = routine,
                        exercises = state.selectedExercises,
                        userId = userId
                    )
                } else {
                    routineRepository.updateRoutineDetails(
                        currentId,
                        routine.name,
                        routine.description ?: ""
                    )
                }
                state = state.copy(isSaved = true)
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }

    fun updateFromMap(values: Map<String, String>) {
        values["name"]?.let { onNameChange(it) }
        values["description"]?.let { onDescriptionChange(it) }
        saveRoutine()
    }

    fun removeExercise(exerciseId: String) {
        val newList = state.selectedExercises.filterNot { it.exerciseId == exerciseId }

        val reindexedList = newList.mapIndexed { index, item ->
            item.copy(order = index)
        }
        state = state.copy(selectedExercises = reindexedList)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LOGICA DE CARGA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    fun loadRoutine(routineId: String?) {
        if (routineId.isNullOrBlank()) return
        state = state.copy(
            routineId = routineId,
            isLoading = true
        )
        viewModelScope.launch {
            try {
                val routine = routineRepository.getRoutineById(routineId)
                if (routine != null) {
                    state = state.copy(
                        name = routine.name,
                        description = routine.description ?: "",
                        selectedExercises = routine.exercises ?: emptyList(),
                        isDataLoaded = true
                    )
                } else {
                    state = state.copy(error = "La rutina no existe en la base de datos")
                }
            } catch (e: Exception) {
                state = state.copy(error = e.message)
            } finally {
                state = state.copy(isLoading = false)
            }
        }
    }
}