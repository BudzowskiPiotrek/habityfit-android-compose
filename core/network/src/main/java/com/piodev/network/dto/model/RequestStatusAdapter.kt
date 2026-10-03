package com.piodev.network.dto.model

import com.piodev.database.model.RequestStatus
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class RequestStatusAdapter {
    @FromJson
    fun fromJson(value: Int): RequestStatus? {
        return when (value) {
            1 -> RequestStatus.PENDING
            2 -> RequestStatus.ACCEPTED
            3 -> RequestStatus.REJECTED
            else -> null
        }
    }

    @ToJson
    fun toJson(status: RequestStatus?): Int? {
        return when (status) {
            RequestStatus.PENDING -> 1
            RequestStatus.ACCEPTED -> 2
            RequestStatus.REJECTED -> 3
            else -> null
        }
    }
}