package com.piodev.network.dto.sync

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class BodyMeasurementDto(
    @Json(name = "ID") val id: String,
    @Json(name = "CREATED_AT") val createdAt: Date,
    @Json(name = "UPDATED_AT") val updatedAt: Date?,
    @Json(name = "DELETED_AT") val deletedAt: Date?,

    @Json(name = "ID_USER") val idUser: String,
    @Json(name = "DATE") val date: Date,
    @Json(name = "WEIGHT") val weight: Double,
    @Json(name = "MUSCLE_MASS") val muscleMass: Double?,
    @Json(name = "FAT_MASS") val fatMass: Double?,
    @Json(name = "BODY_WATER") val bodyWater: Double?,
)