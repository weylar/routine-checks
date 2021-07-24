package com.weylar.routinechecks.local.room

import androidx.room.TypeConverter
import com.weylar.routinechecks.model.Frequency
import com.weylar.routinechecks.model.Status


class Converters {

    @TypeConverter
    fun toFrequency(value: String) = enumValueOf<Frequency>(value)

    @TypeConverter
    fun fromFrequency(value: Frequency) = value.name

    @TypeConverter
    fun toStatus(value: String) = enumValueOf<Status>(value)

    @TypeConverter
    fun fromStatus(value: Status) = value.name

}