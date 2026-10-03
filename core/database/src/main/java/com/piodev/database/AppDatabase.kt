package com.piodev.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.piodev.database.converters.Converters
import com.piodev.database.entities.*
import com.piodev.database.dao.*


@Database(
    entities = [
        UserEntity::class,
        UserRelationEntity::class,
        UserInvitationEntity::class,
        BodyEntity::class,
        ExerciseEntity::class,
        MuscleGroupEntity::class,
        ExerciseMuscleGroupEntity::class,
        RoutineEntity::class,
        RoutineRequestEntity::class,
        RoutineExerciseEntity::class,
        WorkoutSessionEntity::class,
        WorkoutExerciseEntity::class,
        ExerciseSetEntity::class,
        FeedEventEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // 1. PERFIL Y BIOMECANICA **************************
    abstract fun userDao(): UserDao
    abstract fun userRelationDao(): UserRelationDao
    abstract fun userInvitationDao(): UserInvitationDao
    abstract fun bodyDao(): BodyDao

    // 2. CATALOGO **************************************
    abstract fun exerciseDao(): ExerciseDao
    abstract fun muscleGroupDao(): MuscleGroupDao
    abstract fun exerciseMuscleGroupDao(): ExerciseMuscleGroupDao

    // 3. PLANIFICACION RUTINA **************************
    abstract fun routineDao(): RoutineDao
    abstract fun routineRequestDao(): RoutineRequestDao
    abstract fun routineExerciseDao(): RoutineExerciseDao

    // 4. EJECUCION ENTRENAMIENTO ***********************
    abstract fun workoutDao(): WorkoutDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun exerciseSetDao(): ExerciseSetDao

    // 5. FEED DE ACTIVIDAD ******************************
    abstract fun feedEventDao(): FeedEventDao

    // 6. INFORMES ***************************************
    abstract fun informeDao(): InformeDao
}