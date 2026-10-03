package com.piodev.habityfit.core.di

import com.piodev.data.repository.SyncRepository
import com.piodev.data.repository.SyncRepositoryImpl
import com.piodev.habityfit.features.auth.data.repository.AuthRepositoryImpl
import com.piodev.habityfit.features.auth.domain.repository.AuthRepository
import com.piodev.habityfit.features.exercises.data.repository.ExerciseRepositoryImpl
import com.piodev.habityfit.features.exercises.domain.repository.ExerciseRepository
import com.piodev.habityfit.features.community.data.repository.FriendsRepositoryImpl
import com.piodev.habityfit.features.community.data.repository.RoutineRequestRepositoryImpl
import com.piodev.habityfit.features.measurements.data.repository.MeasurementsRepositoryImpl
import com.piodev.habityfit.features.community.data.repository.SettingsRepositoryImpl
import com.piodev.habityfit.features.community.domain.repository.FriendsRepository
import com.piodev.habityfit.features.community.domain.repository.RoutineRequestRepository
import com.piodev.habityfit.features.measurements.domain.repository.MeasurementsRepository
import com.piodev.habityfit.features.community.domain.repository.SettingsRepository
import com.piodev.habityfit.features.feed.data.repository.FeedRepositoryImpl
import com.piodev.habityfit.features.feed.domain.repository.FeedRepository
import com.piodev.habityfit.features.graphics.data.repository.GraphicsRepositoryImpl
import com.piodev.habityfit.features.informe.data.repository.InformeRepositoryImpl
import com.piodev.habityfit.features.graphics.domain.repository.GraphicsRepository
import com.piodev.habityfit.features.informe.domain.repository.InformeRepository
import com.piodev.habityfit.features.routine.data.repository.RoutineRepositoryImpl
import com.piodev.habityfit.features.routine.domain.repository.RoutineRepository
import com.piodev.habityfit.features.workout.data.repository.WorkoutRepositoryImpl
import com.piodev.habityfit.features.workout.domain.repository.WorkoutRepository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // REPOSITORIES DE SUETONIUS
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Binds
    @Singleton
    abstract fun bindSyncRepository(
        syncRepositoryImpl: SyncRepositoryImpl
    ): SyncRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindExerciseRepository(
        exerciseRepositoryImpl: ExerciseRepositoryImpl
    ): ExerciseRepository

    @Binds
    @Singleton
    abstract fun bindRoutineRepository(
        routineRepositoryImpl: RoutineRepositoryImpl
    ): RoutineRepository


    @Binds
    @Singleton
    abstract fun bindWorkoutRepository(
        workoutRepositoryImpl: WorkoutRepositoryImpl
    ): WorkoutRepository

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // REPOSITORIES DE PROFILE
    ////////////////////////////////////////////////////////////////////////////////////////////////
    @Binds
    @Singleton
    abstract fun bindMeasurementsRepository(
        measurementsRepositoryImpl: MeasurementsRepositoryImpl
    ): MeasurementsRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        workoutSettingsImpl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindFriendsRepository(
        friendsRepositoryImpl: FriendsRepositoryImpl
    ): FriendsRepository

    @Binds
    @Singleton
    abstract fun bindFeedEventRepository(
        feedRepositoryImpl: FeedRepositoryImpl
    ): FeedRepository

    @Binds
    @Singleton
    abstract fun bindGraphicsRepository(
        graphicsRepositoryImpl: GraphicsRepositoryImpl
    ): GraphicsRepository

    @Binds
    @Singleton
    abstract fun bindRoutineRequestRepository(
        routineRequestRepositoryImpl: RoutineRequestRepositoryImpl
    ): RoutineRequestRepository

    @Binds
    @Singleton
    abstract fun bindInformeRepository(
        informeRepositoryImpl: InformeRepositoryImpl
    ): InformeRepository
}