package com.piodev.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.piodev.database.AppDatabase
import com.piodev.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `USER_INVITATION` (
                `ID` TEXT NOT NULL, 
                `CREATED_AT` INTEGER NOT NULL, 
                `UPDATED_AT` INTEGER, 
                `DELETED_AT` INTEGER, 
                `SYNC_STATUS` TEXT NOT NULL, 
                `EMAIL` TEXT NOT NULL, 
                `TOKEN` TEXT, 
                `SENDER_USER_ID` TEXT NOT NULL, 
                `EXPIRES_AT` INTEGER, 
                `STATUS` TEXT NOT NULL, 
                `SENDED` INTEGER NOT NULL, 
                PRIMARY KEY(`ID`), 
                FOREIGN KEY(`SENDER_USER_ID`) REFERENCES `USERS`(`ID`) ON UPDATE NO ACTION ON DELETE NO ACTION
            )
        """.trimIndent()
        )

        db.execSQL("CREATE INDEX IF NOT EXISTS `index_USER_INVITATION_SENDER_USER_ID` ON `USER_INVITATION` (`SENDER_USER_ID`)")
    }
}
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `USERS` ADD COLUMN `TERMS_ACCEPTED_AT` INTEGER")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `MUSCLE_GROUP` ADD COLUMN `MIN_SETS` INTEGER")
        db.execSQL("ALTER TABLE `MUSCLE_GROUP` ADD COLUMN `MAX_SETS` INTEGER")
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "setpoint_db"
        )
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            .addMigrations(MIGRATION_3_4)
            .build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    fun provideUserRelationDao(db: AppDatabase): UserRelationDao = db.userRelationDao()

    @Provides
    fun provideUserInvitation(db: AppDatabase): UserInvitationDao = db.userInvitationDao()

    @Provides
    fun provideBodyDao(db: AppDatabase): BodyDao = db.bodyDao()

    @Provides
    fun provideExerciseDao(db: AppDatabase): ExerciseDao = db.exerciseDao()

    @Provides
    fun provideMuscleGroupDao(db: AppDatabase): MuscleGroupDao = db.muscleGroupDao()

    @Provides
    fun provideExerciseMuscleGroupDao(db: AppDatabase): ExerciseMuscleGroupDao =
        db.exerciseMuscleGroupDao()

    @Provides
    fun provideRoutineDao(db: AppDatabase): RoutineDao = db.routineDao()

    @Provides
    fun provideRoutineRequestDao(db: AppDatabase): RoutineRequestDao = db.routineRequestDao()

    @Provides
    fun provideRoutineExerciseDao(db: AppDatabase): RoutineExerciseDao = db.routineExerciseDao()

    @Provides
    fun provideWorkoutDao(db: AppDatabase): WorkoutDao = db.workoutDao()

    @Provides
    fun provideWorkoutExerciseDao(db: AppDatabase): WorkoutExerciseDao = db.workoutExerciseDao()

    @Provides
    fun provideExerciseSetDao(db: AppDatabase): ExerciseSetDao = db.exerciseSetDao()

    @Provides
    fun provideFeedEventDao(db: AppDatabase): FeedEventDao = db.feedEventDao()

    @Provides
    fun provideInformeDao(db: AppDatabase): InformeDao = db.informeDao()
}
