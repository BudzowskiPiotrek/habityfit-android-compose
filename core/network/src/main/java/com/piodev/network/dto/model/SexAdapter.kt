package com.piodev.network.dto.model

import com.piodev.database.model.Sex
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class SexAdapter {
    @FromJson
    fun fromJson(value: Int): Sex? {
        return when (value) {
            1 -> Sex.MEN
            2 -> Sex.WOMEN
            else -> null
        }
    }

    @ToJson
    fun toJson(sex: Sex?): Int? {
        return sex?.value
    }
}