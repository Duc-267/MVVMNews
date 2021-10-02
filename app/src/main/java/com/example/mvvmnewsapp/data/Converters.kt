package com.example.mvvmnewsapp.data

import androidx.room.TypeConverter
import com.example.mvvmnewsapp.model.Source


class Converters {

    @TypeConverter
    fun toString(source:com.example.mvvmnewsapp.model.Source):String{
        return source.name
    }

    @TypeConverter
    fun toSource(name: String):Source{
        return Source(name, name)
    }
}