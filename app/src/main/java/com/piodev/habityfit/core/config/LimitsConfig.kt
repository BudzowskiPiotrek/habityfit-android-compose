package com.piodev.habityfit.core.config

import com.piodev.habityfit.BuildConfig

import java.util.Calendar
import java.util.TimeZone

object LimitsConfig {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // APP DATES
    const val TOKEN_APP = BuildConfig.TOKEN_APP
    const val EMAIL_APP = BuildConfig.EMAIL_APP
    const val LINK_APK = BuildConfig.LINK_APK

    val EXPIRATION_DATE: Long = Calendar.getInstance().apply {
        set(2026, Calendar.NOVEMBER, 1)
    }.timeInMillis

    val TERMS_REQUIRED_DATE: Long =
        Calendar.getInstance(TimeZone.getTimeZone("Europe/Madrid")).apply {
            set(2026, Calendar.JULY, 3, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // AUTH LIMITS
    const val MIN_PASSWORD_LENGTH = 12
    const val MAX_PASSWORD_LENGTH = 64
    val PASSWORD_REGEX = Regex("""^(?=.*[A-Z])(?=.*[0-9])[^\'"]{12,64}$""")

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // COMMUNITY LIMITS
    val EMAIL_REGEX = Regex("""^[a-zA-Z0-9._%+\-]+@[a-zA-Z0-9.\-]+\.[a-zA-Z]{2,}$""")
    val GUID_REGEX =
        Regex("""^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$""")

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ROUTINE LIMITS
    const val MAX_ROUTINE_NAME = 20 // LENGTH
    const val MAX_ROUTINE_DESC = 200 // LENGTH
    const val MAX_SETS = 500
    const val MAX_REPS = 500
    const val MAX_REST_SECONDS = 900
    const val MAX_EXERCISE_WEIGHT = 600.0

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // WORKOUT LIMITS
    const val MAX_WORKOUT_DURATION = 300
    const val MAX_WORKOUT_NOTES = 200 // LENGTH
    const val MAX_SET_WEIGHT = 600.0
    const val MAX_SET_REPS = 500
    const val MAX_SET_RPE = 10.0

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // MEASUREMENTS LIMITS
    const val MAX_HUMAN_WEIGHT = 999.0
    const val MAX_HUMAN_MUSCLE = 999.0
    const val MAX_HUMAN_FAT = 999.0
    const val MAX_HUMAN_WATER = 999.0

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // RM LIMITS
    const val MAX_RM_WEIGHT = 999.0
    const val MAX_RM_REPS = 14

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BRZYCKI LIMITS
    const val BRZYCKI_CONST_1 = 1.0278
    const val BRZYCKI_CONST_2 = 0.0278
    const val MAX_ALLOWED_REPS = 14
    const val MAX_ALLOWED_WEIGHT = 999.0
}