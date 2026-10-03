package com.piodev.network.dto.sync

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SyncPayloadDto(
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Información de autenticación y usuario
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Json(name = "token") val token: String? = null,
    @Json(name = "users") val users: List<UserDto> = emptyList(),
    @Json(name = "userRelations") val userRelations: List<UserRelationDto> = emptyList(),
    @Json(name = "userInvitations") val userInvitations: List<UserInvitationDto> = emptyList(),

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Datos relacionados con el progreso físico del usuario
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Json(name = "bodyMeasurements") val bodyMeasurements: List<BodyMeasurementDto> = emptyList(),

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Datos relacionados con la actividad física del usuario
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Json(name = "exercises") val exercises: List<ExerciseDto> = emptyList(),
    @Json(name = "muscleGroups") val muscleGroups: List<MuscleGroupDto> = emptyList(),
    @Json(name = "exerciseMuscleGroups") val exerciseMuscleGroups: List<ExerciseMuscleGroupDto> = emptyList(),

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Datos relacionados con las rutinas y sesiones de entrenamiento
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Json(name = "routines") val routines: List<RoutineDto> = emptyList(),
    @Json(name = "routineRequests") val routineRequests: List<RoutineRequestDto> = emptyList(),
    @Json(name = "routineExercises") val routineExercises: List<RoutineExerciseDto> = emptyList(),

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Datos relacionados con las sesiones de entrenamiento y los ejercicios realizados
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Json(name = "workoutSessions") val workoutSessions: List<WorkoutSessionDto> = emptyList(),
    @Json(name = "workoutExercises") val workoutExercises: List<WorkoutExerciseDto> = emptyList(),
    @Json(name = "exerciseSets") val exerciseSets: List<ExerciseSetDto> = emptyList(),

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Datos relacionados con el feed de actividad del usuario
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Json(name = "feedEvents") val feedEvents: List<FeedEventDto> = emptyList(),
)