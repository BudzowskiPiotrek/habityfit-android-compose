package com.piodev.habityfit.features.workout.data.repository

import com.piodev.database.dao.ExerciseMuscleGroupDao
import com.piodev.database.dao.ExerciseSetDao
import com.piodev.database.dao.MuscleGroupDao
import com.piodev.database.dao.RoutineDao
import com.piodev.database.dao.UserDao
import com.piodev.database.dao.WorkoutDao
import com.piodev.database.dao.WorkoutExerciseDao
import com.piodev.database.entities.ExerciseEntity
import com.piodev.database.entities.ExerciseSetEntity
import com.piodev.database.entities.WorkoutExerciseEntity
import com.piodev.database.entities.WorkoutSessionEntity
import com.piodev.database.model.FeedEventType
import com.piodev.database.model.SyncStatus
import com.piodev.habityfit.features.feed.domain.repository.FeedRepository
import com.piodev.habityfit.features.routine.data.mapper.toDomain
import com.piodev.habityfit.features.workout.data.mapper.toDomain
import com.piodev.habityfit.features.workout.domain.model.WorkoutSession
import com.piodev.habityfit.features.workout.domain.model.WorkoutSet
import com.piodev.habityfit.features.workout.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workDao: WorkoutDao,
    private val routineDao: RoutineDao,
    private val exerciseDao: WorkoutExerciseDao,
    private val setDao: ExerciseSetDao,
    private val feedRepository: FeedRepository,
    private val userDao: UserDao,
    private val exerciseMuscleGroupDao: ExerciseMuscleGroupDao,
    private val muscleGroupDao: MuscleGroupDao
) : WorkoutRepository {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // LECTURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun getWorkoutById(id: String): WorkoutSession? {
        return workDao.getWorkoutById(id)?.toDomain()
    }
     
    override fun getSessions(userId: String): Flow<List<WorkoutSession>> {
        return workDao.getWorkoutHistory(userId).map { list -> list.map { it.toDomain() } }
    }

    override fun getSessionWithExercises(sessionId: String): Flow<WorkoutSession?> {
        return workDao.getSessionByIdFlow(sessionId).map { it?.toDomain() }
    }

    override fun getSetsForExercise(workoutExerciseId: String): Flow<List<WorkoutSet>> {
        return setDao.getSetsByWorkoutExercise(workoutExerciseId)
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun getRestSecondsForExercise(workoutExerciseId: String): Int {
        return exerciseDao.getRestSeconds(workoutExerciseId) ?: 0
    }

    override suspend fun getExerciseByWorkoutExerciseId(workoutExerciseId: String): ExerciseEntity? {
        return workDao.getExerciseByWorkoutExerciseId(workoutExerciseId)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ESCRITURA
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun updateSet(
        workoutExerciseId: String,
        set: WorkoutSet
    ) {
        val entity = ExerciseSetEntity(
            ID = set.id,
            workoutExerciseId = workoutExerciseId,
            setNumber = set.setNumber,
            reps = set.reps,
            weight = set.weight ?: 0.0,
            rpe = set.rpe,
            CREATED_AT = System.currentTimeMillis(),
            UPDATED_AT = System.currentTimeMillis(),
            DELETED_AT = null,
            SYNC_STATUS = SyncStatus.PENDING
        )
        setDao.updateSet(entity)
    }

    override suspend fun saveWorkoutSession(
        userId: String,
        routineId: String,
        session: WorkoutSession
    ) {
        val sessionId = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Fase 1: Crear la sesión de entrenamiento

        val sessionEntity = WorkoutSessionEntity(
            ID = sessionId,
            userId = userId,
            date = session.date,
            durationMinutes = session.durationMinutes,
            notes = session.notes,
            CREATED_AT = now,
            UPDATED_AT = now,
            DELETED_AT = null,
            SYNC_STATUS = SyncStatus.PENDING
        )
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Fase 2: obtener exercicios de la rutina y creamos lista de ejercicios y sets a insertar

        val routineWithEx = routineDao.getRoutineWithExercises(routineId)
            .map { it.toDomain() }.first() ?: return

        val exercisesToInsert = mutableListOf<WorkoutExerciseEntity>()
        val setsToInsert = mutableListOf<ExerciseSetEntity>()
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Fase 3: Crear entidades de ejercicios y sets para cada ejercicio de la rutina

        routineWithEx.exercises.forEachIndexed { index, r ->
            val workoutExerciseId = UUID.randomUUID().toString()

            exercisesToInsert.add(
                WorkoutExerciseEntity(
                    ID = workoutExerciseId,
                    sessionId = sessionId,
                    exerciseId = r.exerciseId,
                    order = index,
                    restSecond = r.restSecond,
                    CREATED_AT = now,
                    UPDATED_AT = now,
                    DELETED_AT = null,
                    SYNC_STATUS = SyncStatus.PENDING
                )
            )
            ////////////////////////////////////////////////////////////////////////////////////////////
            // Fase 4: Logica de cargar Pr-s de otra session

            val lastExId = workDao.getLastWorkoutExerciseId(r.exerciseId, index)
            val lastSets = lastExId?.let { setDao.getSetsByWorkoutExercise(it).first() }
            ////////////////////////////////////////////////////////////////////////////////////////////
            // Fase 5: Crear entidades de sets para cada ejercicio

            (1..r.sets).forEach { currentSetNumber ->
                val previousSet = lastSets?.find { it.setNumber == currentSetNumber }

                setsToInsert.add(
                    ExerciseSetEntity(
                        ID = UUID.randomUUID().toString(),
                        workoutExerciseId = workoutExerciseId,
                        setNumber = currentSetNumber,
                        reps = previousSet?.reps ?: 0,    // Si existe historial lo pone, si no 0
                        weight = previousSet?.weight
                            ?: 0.0, // Si existe historial lo pone, si no 0.0
                        rpe = null,
                        CREATED_AT = now,
                        UPDATED_AT = now,
                        DELETED_AT = null,
                        SYNC_STATUS = SyncStatus.PENDING
                    )
                )
            }
        }

        workDao.insert(sessionEntity)
        if (exercisesToInsert.isNotEmpty())
            exerciseDao.insertWorkoutExercises(exercisesToInsert)
        if (setsToInsert.isNotEmpty())
            setDao.insertSets(setsToInsert)

        val fullName = userDao.getUserById(userId)?.fullName ?: ""
        val customText = buildSessionFeedText(
            fullName,
            userId,
            routineWithEx.exercises.map { it.exerciseId },
            now
        )
        val firstExerciseId = routineWithEx.exercises.firstOrNull()?.exerciseId
        if (firstExerciseId != null) {
            feedRepository.createFeedEvent(
                userId = userId,
                eventType = FeedEventType.START_SESSION,
                customText = customText,
                exerciseId = firstExerciseId
            )
        }
    }

    override suspend fun updateWorkoutDetails(
        id: String,
        date: Long,
        duration: Int,
        notes: String?
    ) {
        workDao.updateWorkoutDetails(
            id,
            date,
            duration,
            notes,
            SyncStatus.PENDING,
            System.currentTimeMillis()
        )
    }

    override suspend fun updateWorkoutDuration(sessionId: String, duration: Int) {
        workDao.updateWorkoutDuration(
            sessionId,
            duration,
            update = System.currentTimeMillis()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BORRAR
    ////////////////////////////////////////////////////////////////////////////////////////////////
    override suspend fun deleteWorkoutSession(sessionId: String) {
        workDao.softDeleteById(
            sessionId,
            System.currentTimeMillis()
        )
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // FUNCIONES PRIVADAS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private suspend fun buildSessionFeedText(
        fullName: String,
        userId: String,
        exerciseIds: List<String>,
        now: Long
    ): String {
        val LEG_GROUPS = setOf(
            "Glúteo", "Aductores", "Cuádriceps", "Isquiotibiales", "Gastrocnemios"
        )
        val UPPER_GROUPS = setOf(
            "Músculos del Cuello", "Lumbar", "Deltoides Posterior", "Trapecio",
            "Antebrazos", "Pectorales", "Dorsal Ancho", "Bíceps Braquial",
            "Tríceps Braquial", "Abdominales", "Deltoides Anterolateral"
        )

        var hasLeg = false
        var hasUpper = false

        exerciseIds.forEach { exerciseId ->
            val muscleIds = exerciseMuscleGroupDao.getMuscleGroupsByExercise(exerciseId)
            muscleIds.forEach { muscleId ->
                val muscle = muscleGroupDao.getMuscleGroupById(muscleId)
                val name = muscle?.name
                if (name != null) {
                    if (LEG_GROUPS.contains(name)) hasLeg = true
                    if (UPPER_GROUPS.contains(name)) hasUpper = true
                }
            }
        }

        val grupo = when {
            hasLeg && hasUpper -> "Full Body"
            hasLeg -> "Tren Inferior"
            hasUpper -> "Tren Superior"
            else -> "Cardio"
        }

        val cal = Calendar.getInstance().apply { timeInMillis = now }
        cal.firstDayOfWeek = Calendar.MONDAY
        val currentDay = cal.get(Calendar.DAY_OF_WEEK)
        val daysFromMonday = (currentDay - Calendar.MONDAY + 7) % 7
        cal.add(Calendar.DAY_OF_MONTH, -daysFromMonday)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val monday = cal.timeInMillis
        cal.add(Calendar.DAY_OF_MONTH, 7)
        cal.add(Calendar.MILLISECOND, -1)
        val sunday = cal.timeInMillis

        val sessionCount = workDao.countSessionsBetween(userId, monday, sunday)

        return "$fullName completó $grupo.\n¡Sesión $sessionCount de la semana adentro!"
    }
}