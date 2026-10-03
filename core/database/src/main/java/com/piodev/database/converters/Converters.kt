package com.piodev.database.converters

import androidx.room.TypeConverter
import com.piodev.database.model.FeedEventType
import com.piodev.database.model.InvitationStatus
import com.piodev.database.model.RelationStatus
import com.piodev.database.model.RequestStatus
import com.piodev.database.model.Sex
import com.piodev.database.model.SyncStatus

class Converters {

    @TypeConverter
    fun fromFeedEventType(value: FeedEventType?): String? = value?.name

    @TypeConverter
    fun toFeedEventType(value: String?): FeedEventType? = value?.let { FeedEventType.valueOf(it) }

    @TypeConverter
    fun fromSex(value: Sex?): String? = value?.name

    @TypeConverter
    fun toSex(value: String?): Sex? = value?.let { Sex.valueOf(it) }

    @TypeConverter
    fun fromSyncStatus(value: SyncStatus?): String? = value?.name

    @TypeConverter
    fun toSyncStatus(value: String?): SyncStatus? = value?.let { SyncStatus.valueOf(it) }

    @TypeConverter
    fun fromRequestStatus(value: RequestStatus?): String? = value?.name

    @TypeConverter
    fun toRequestStatus(value: String?): RequestStatus? =
        value?.let { RequestStatus.valueOf(it) }

    // RelationStatus
    @TypeConverter
    fun fromRelationStatus(value: RelationStatus?): String? = value?.name

    @TypeConverter
    fun toRelationStatus(value: String?): RelationStatus? =
        value?.let { RelationStatus.valueOf(it) }

    // InvitationStatus
    @TypeConverter
    fun fromInvitationStatus(value: InvitationStatus?): String? = value?.name

    @TypeConverter
    fun toInvitationStatus(value: String?): InvitationStatus? =
        value?.let { InvitationStatus.valueOf(it) }
}