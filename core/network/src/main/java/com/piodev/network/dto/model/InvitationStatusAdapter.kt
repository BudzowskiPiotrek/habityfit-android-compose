package com.piodev.network.dto.model

import com.piodev.database.model.InvitationStatus
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class InvitationStatusAdapter {
    @FromJson
    fun fromJson(value: Int): InvitationStatus? {
        return when (value) {
            1 -> InvitationStatus.PENDING
            2 -> InvitationStatus.ACCEPTED
            3 -> InvitationStatus.EXPIRED
            else -> null
        }
    }

    @ToJson
    fun toJson(status: InvitationStatus?): Int? {
        return when (status) {
            InvitationStatus.PENDING -> 1
            InvitationStatus.ACCEPTED -> 2
            InvitationStatus.EXPIRED-> 3
            else -> null
        }
    }
}