package com.piodev.network.dto.sync

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class ExerciseDto(
    @Json(name = "ID") val id: String,
    @Json(name = "CREATED_AT") val createdAt: Date,
    @Json(name = "UPDATED_AT") val updatedAt: Date?,
    @Json(name = "DELETED_AT") val deletedAt: Date?,

    @Json(name = "NAME") val name: String,
    @Json(name = "DESCRIPTION") val description: String?,
    @Json(name = "IMAGE_URL") val imageUrl: String?,
    @Json(name = "EQUIPMENT_TYPE") val equipmentType: String?,
)