package com.surya_yasa_antariksa.crude_comerse.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.util.*

class TimeConverter  {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }


}