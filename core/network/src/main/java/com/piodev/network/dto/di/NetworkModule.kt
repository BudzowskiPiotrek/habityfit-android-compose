package com.piodev.network.di

import com.piodev.network.api.FitnessApiService
import com.piodev.network.dto.model.FeedEventAdapter
import com.piodev.network.dto.model.InvitationStatusAdapter
import com.piodev.network.dto.model.RelationStatusAdapter
import com.piodev.network.dto.model.RequestStatusAdapter
import com.piodev.network.dto.model.SexAdapter
import com.piodev.network.interceptor.AuthInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.Date
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.piodev.network.BuildConfig


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = BuildConfig.BASE_URL
    /**
     * EL TRADUCTOR (Moshi):
     * Se encarga de convertir el texto JSON que viene del servidor en objetos Kotlin (Data Classes)
     * y viceversa. Sin esto, Android no entendería los datos de la API.
     */
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(
                Date::class.java,
                Rfc3339DateJsonAdapter().nullSafe()
            )
            .add(FeedEventAdapter())
            .add(SexAdapter())
            .add(RequestStatusAdapter())
            .add(RelationStatusAdapter())
            .add(InvitationStatusAdapter())
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    /**
     * EL MOTOR DE ENVÍO (OkHttpClient):
     * Es el "navegador" interno. Maneja la conexión física, los tiempos de espera y la seguridad.
     * Aquí inyectamos el AuthInterceptor para que Hilt lo gestione correctamente.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val loggingInterceptor =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        return OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    /**
     * EL ORQUESTADOR (Retrofit):
     * Une el "Motor" (OkHttp) con el "Traductor" (Moshi) y le dice a dónde ir (BASE_URL).
     * Es la pieza central que configura cómo se realizarán todas las llamadas.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    /**
     * EL PRODUCTO FINAL (FitnessApiService):
     * Esta es la herramienta que realmente usarás en tus Repositorios.
     * Retrofit lee tu interfaz y genera el código real para ejecutar las peticiones.
     */
    @Provides
    @Singleton
    fun provideFitnessApiService(retrofit: Retrofit): FitnessApiService {
        return retrofit.create(FitnessApiService::class.java)
    }
}