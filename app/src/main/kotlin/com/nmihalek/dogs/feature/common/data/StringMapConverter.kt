package com.nmihalek.dogs.feature.common.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object StringMapConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String?): Map<String, List<String>> {
        val mapType = object : TypeToken<Map<String?, List<String>?>?>() {}.type
        return gson.fromJson(value, mapType)
    }

    @TypeConverter
    fun fromStringMap(map: Map<String?, List<String>?>?): String {
        return gson.toJson(map)
    }
}