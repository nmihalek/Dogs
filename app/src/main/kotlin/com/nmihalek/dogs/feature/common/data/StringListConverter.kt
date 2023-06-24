package com.nmihalek.dogs.feature.common.data

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson

object StringListConverter {

    @TypeConverter
    fun fromString(value: String?): List<String> =
        value?.split(",")?.toList().orEmpty()

    @TypeConverter
    fun toString(list: List<String?>?): String =
        list?.joinToString(",").orEmpty()
}