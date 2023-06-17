package com.rupinder.travelbook.roomdb

import androidx.room.TypeConverter
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromList(value : List<String>) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String) = Json.decodeFromString<List<String>>(value)
}