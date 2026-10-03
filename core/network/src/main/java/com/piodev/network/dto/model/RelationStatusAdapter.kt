package com.piodev.network.dto.model

import com.piodev.database.model.RelationStatus
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class RelationStatusAdapter {
    @FromJson
    fun fromJson(value: Int): RelationStatus? {
        return when (value) {
            1 -> RelationStatus.PENDING
            2 -> RelationStatus.ACCEPTED
            3 -> RelationStatus.REJECTED
            else -> null
        }
    }

    @ToJson
    fun toJson(status: RelationStatus?): Int? {
        return when (status) {
            RelationStatus.PENDING -> 1
            RelationStatus.ACCEPTED -> 2
            RelationStatus.REJECTED -> 3
            else -> null
        }
    }
}