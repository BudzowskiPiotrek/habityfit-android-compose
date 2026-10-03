package com.piodev.data.repository

import com.piodev.database.dao.*
import com.piodev.network.api.FitnessApiService
import com.piodev.network.dto.sync.SyncPayloadDto
import com.piodev.data.mapper.*
import com.piodev.network.dto.common.SessionManager
import com.piodev.network.dto.request.SyncRequestDto
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val apiService: FitnessApiService,
    private val userDao: UserDao,
    private val bodyDao: BodyDao,
    private val exerciseDao: ExerciseDao,
    private val routineDao: RoutineDao,
    private val workoutDao: WorkoutDao,
    private val routineExerciseDao: RoutineExerciseDao,
    private val workoutExerciseDao: WorkoutExerciseDao,
    private val exerciseSetDao: ExerciseSetDao,
    private val muscleGroupDao: MuscleGroupDao,
    private val exerciseMuscleGroupDao: ExerciseMuscleGroupDao,
    private val sessionManager: SessionManager,
    private val routineRequestDao: RoutineRequestDao,
    private val userRelationDao: UserRelationDao,
    private val userInvitationDto: UserInvitationDao,
    private val feedDao: FeedEventDao
) : SyncRepository {

    override suspend fun pushLocalChanges(): Result<Unit> = runCatching {
        ////////////////////////////////////////////////////////////////////////////////////////////
        // 1. Obtenemos los cambios locales pendientes (SyncStatus.PENDING)
        val pUser = userDao.getPendingSync()
        val pBody = bodyDao.getPendingSync()
        val pMuscleGroups = muscleGroupDao.getPendingSync()
        val pExercises = exerciseDao.getPendingSync()
        val pExMuscle = exerciseMuscleGroupDao.getPendingSync()
        val pRoutines = routineDao.getPendingSync()
        val pRoutineEx = routineExerciseDao.getPendingSync()
        val pSessions = workoutDao.getPendingSync()
        val pWorkoutEx = workoutExerciseDao.getPendingSync()
        val pSets = exerciseSetDao.getPendingSync()
        val pRelations = userRelationDao.getPendingSync()
        val pRequests = routineRequestDao.getPendingSync()
        val pInvitations = userInvitationDto.getPendingSync()
        val pFeed = feedDao.getPendingSync()

        ////////////////////////////////////////////////////////////////////////////////////////////
        // Si no hay cambios, podemos salir temprano

        if (pUser.isEmpty() && pBody.isEmpty() && pMuscleGroups.isEmpty() && pExercises.isEmpty() &&
            pRoutines.isEmpty() && pSessions.isEmpty() && pSets.isEmpty() &&
            pRequests.isEmpty() && pRelations.isEmpty() && pRoutineEx.isEmpty() && pWorkoutEx.isEmpty()
            && pFeed.isEmpty() && pInvitations.isEmpty()
        ) return@runCatching
        ////////////////////////////////////////////////////////////////////////////////////////////
        // 2. Empaquetamos en el DTO de transporte (Payload)

        val payload = SyncPayloadDto(
            users = pUser.map { it.toDto() },
            bodyMeasurements = pBody.map { it.toDto() },
            muscleGroups = pMuscleGroups.map { it.toDto() },
            exercises = pExercises.map { it.toDto() },
            exerciseMuscleGroups = pExMuscle.map { it.toDto() },
            routines = pRoutines.map { it.toDto() },
            routineExercises = pRoutineEx.map { it.toDto() },
            workoutSessions = pSessions.map { it.toDto() },
            workoutExercises = pWorkoutEx.map { it.toDto() },
            exerciseSets = pSets.map { it.toDto() },
            userRelations = pRelations.map { it.toDto() },
            userInvitations = pInvitations.map { it.toDto() },
            routineRequests = pRequests.map { it.toDto() },
            feedEvents = pFeed.map { it.toDto() }
        )
        ////////////////////////////////////////////////////////////////////////////////////////////
        // 3. Envío masivo (Bulk)

        val response = apiService.uploadSyncData(payload)

        if (response.isSuccessful) {
            val syncResult = response.body()?.result
            if (syncResult != null) {
                val successfulIds =
                    syncResult.itemIds.zip(syncResult.success).filter { it.second }.map { it.first }

                if (successfulIds.isNotEmpty()) {
                    userDao.markAsSynced(pUser.filter { successfulIds.contains(it.ID.toString()) }
                        .map { it.ID })
                    bodyDao.markAsSynced(pBody.filter { successfulIds.contains(it.ID.toString()) }
                        .map { it.ID })
                    muscleGroupDao.markAsSynced(pMuscleGroups.filter { successfulIds.contains(it.ID.toString()) }
                        .map { it.ID })
                    exerciseDao.markAsSynced(pExercises.filter { successfulIds.contains(it.ID.toString()) }
                        .map { it.ID })
                    exerciseMuscleGroupDao.markAsSynced(pExMuscle.filter { successfulIds.contains(it.ID.toString()) }
                        .map { it.ID })
                    routineDao.markAsSynced(pRoutines.filter { successfulIds.contains(it.ID.toString()) }
                        .map { it.ID })
                    routineExerciseDao.markAsSynced(pRoutineEx.filter { successfulIds.contains(it.ID.toString()) }
                        .map { it.ID })
                    workoutDao.markAsSynced(pSessions.filter { successfulIds.contains(it.ID.toString()) }
                        .map { it.ID })
                    workoutExerciseDao.markAsSynced(pWorkoutEx.filter { successfulIds.contains(it.ID.toString()) }
                        .map { it.ID })
                    exerciseSetDao.markAsSynced(pSets.filter { successfulIds.contains(it.ID.toString()) }
                        .map { it.ID })
                    userRelationDao.markAsSynced(pRelations.filter { successfulIds.contains(it.ID.toString()) }
                        .map { it.ID })
                    userInvitationDto.markAsSynced(pInvitations.filter { successfulIds.contains(it.ID.toString()) }
                        .map { it.ID })
                    routineRequestDao.markAsSynced(pRequests.filter { successfulIds.contains(it.ID.toString()) }
                        .map { it.ID })
                    feedDao.markAsSynced(pFeed.filter { successfulIds.contains(it.ID.toString()) }
                        .map { it.ID })
                }
            }
        } else {
            throw Exception("Error en sincronización remota: ${response.code()}")
        }
    }

    override suspend fun pullRemoteChanges(): Result<Unit> = runCatching {
        ////////////////////////////////////////////////////////////////////////////////////////////
        // 1. Obtenemos la información necesaria para la consulta (userId, lastSyncDate)

        val user = userDao.getUserById(sessionManager.getUserId().toString())
        if (user == null) return@runCatching
        val userId = user.ID
        val lastSyncDate = (user.lastSyncDate) ?: 0L
        ////////////////////////////////////////////////////////////////////////////////////////////
        // 2. Realizamos la consulta al endpoint de Pull

        val request = SyncRequestDto(userId = userId, lastSync = java.util.Date(lastSyncDate))
        val response = apiService.downloadSyncData(request)
        ////////////////////////////////////////////////////////////////////////////////////////////
        // Si la respuesta es exitosa, procesamos los datos recibidos

        if (!response.isSuccessful) throw Exception("Error en Pull: ${response.code()}")
        ////////////////////////////////////////////////////////////////////////////////////////////
        // 3. Extraemos el payload del response y actualizamos el token si viene en la respuesta

        val remoteData = response.body() ?: return@runCatching
        val payload = remoteData.result ?: return@runCatching

        val token = payload.token
        if (!token.isNullOrEmpty()) {
            sessionManager.saveToken(token)
        }
        ////////////////////////////////////////////////////////////////////////////////////////////
        // 4. Actualizamos la base de datos local con los datos remotos (Insert/Update)

        if (payload.users.isNotEmpty())
            userDao.insertAll(payload.users.map { it.toEntity() })
        if (payload.bodyMeasurements.isNotEmpty())
            bodyDao.insertAll(payload.bodyMeasurements.map { it.toEntity() })
        if (payload.muscleGroups.isNotEmpty())
            muscleGroupDao.insertAll(payload.muscleGroups.map { it.toEntity() })
        if (payload.exercises.isNotEmpty())
            exerciseDao.insertAll(payload.exercises.map { it.toEntity() })
        if (payload.exerciseMuscleGroups.isNotEmpty())
            exerciseMuscleGroupDao.insertAll(payload.exerciseMuscleGroups.map { it.toEntity() })
        if (payload.routines.isNotEmpty())
            routineDao.insertAll(payload.routines.map { it.toEntity() })
        if (payload.routineExercises.isNotEmpty())
            routineExerciseDao.insertAll(payload.routineExercises.map { it.toEntity() })
        if (payload.workoutSessions.isNotEmpty())
            workoutDao.insertAll(payload.workoutSessions.map { it.toEntity(userId) })
        if (payload.workoutExercises.isNotEmpty())
            workoutExerciseDao.insertAll(payload.workoutExercises.map { it.toEntity(it.sessionId) })
        if (payload.exerciseSets.isNotEmpty())
            exerciseSetDao.insertAll(payload.exerciseSets.map { it.toEntity(it.workoutExerciseId) })
        if (payload.userRelations.isNotEmpty())
            userRelationDao.insertAll(payload.userRelations.map { it.toEntity() })
        if (payload.routineRequests.isNotEmpty())
            routineRequestDao.insertAll(payload.routineRequests.map { it.toEntity() })
        if (payload.feedEvents.isNotEmpty())
            feedDao.insertAll(payload.feedEvents.map { it.toEntity() })
        ////////////////////////////////////////////////////////////////////////////////////////////
        // 5. Actualización del Ancla Temporal

        userDao.updateLastSyncDate(userId, System.currentTimeMillis())
    }

    override suspend fun syncAll(): Result<Unit> {
        return try {
            ////////////////////////////////////////////////////////////////////////////////////////
            // Primero empujamos los cambios locales, luego tiramos los remotos

            pushLocalChanges().getOrThrow()
            pullRemoteChanges().getOrThrow()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}