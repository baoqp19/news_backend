package com.example.news_backend.data.source.db

import androidx.room.TypeConverter
import com.example.news_backend.data.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source) :String {
        return source.name
    }
    @TypeConverter
    fun toSource(name: String) : Source {
        return Source(name, name)
    }

}
