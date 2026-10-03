package com.piodev.data.model

data class RoutineExercise(
    val id: String,          // El ID propio de esta relación (BaseEntity)
    val routineId: String,   // A qué rutina pertenece
    val exerciseId: String,  // Qué ejercicio es
    val order: Int,
    val sets: Int,
    val reps: Int,
    val targetWeight: Double?,
    val restSecond: Int
)