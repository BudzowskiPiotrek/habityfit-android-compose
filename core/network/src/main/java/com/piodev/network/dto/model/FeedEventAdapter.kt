package com.piodev.network.dto.model

import com.piodev.database.model.FeedEventType
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class FeedEventAdapter {
    @FromJson
    fun fromJson(value: Int): FeedEventType? {
        return when (value) {
            1 -> FeedEventType.START_SESSION
            2 -> FeedEventType.NEW_RECORD
            3 -> FeedEventType.CHALLENGE_WIN
            4 -> FeedEventType.GENERAL
            else -> null
        }
    }

    @ToJson
    fun toJson(event: FeedEventType?): Int? {
        return when (event) {
            FeedEventType.START_SESSION -> 1
            FeedEventType.NEW_RECORD -> 2
            FeedEventType.CHALLENGE_WIN -> 3
            FeedEventType.GENERAL -> 4
            else -> null
        }
    }
}
