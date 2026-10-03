package com.piodev.network.dto.sync

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class MuscleGroupDto(
    @Json(name = "ID") val id: String,
    @Json(name = "CREATED_AT") val createdAt: Date,
    @Json(name = "UPDATED_AT") val updatedAt: Date?,
    @Json(name = "DELETED_AT") val deletedAt: Date?,

    @Json(name = "NAME") val name: String,
    @Json(name = "DESCRIPTION") val description: String?,
    @Json(name = "MIN_SETS") val minSets: Int? = null,
    @Json(name = "MAX_SETS") val maxSets: Int? = null,
)