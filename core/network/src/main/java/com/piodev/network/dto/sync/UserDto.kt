package com.piodev.network.dto.sync

import com.piodev.database.model.Sex
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "ID") val id: String,
    @Json(name = "CREATED_AT") val createdAt: Date,
    @Json(name = "UPDATED_AT") val updatedAt: Date?,
    @Json(name = "DELETED_AT") val deletedAt: Date?,

    @Json(name = "EMAIL") val email: String,
    @Json(name = "NAME") val fullName: String,
    @Json(name = "SEX") val sex: Sex?,
    @Json(name = "BIRTH_DATE") val birthDate: Date?,
    @Json(name = "HEIGHT") val height: Double?,
    @Json(name = "TERMS_ACCEPTED_AT") val termsAcceptedAt: Date?
)