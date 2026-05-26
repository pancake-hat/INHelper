package com.example.inhelper.data.local

import android.content.Context
import androidx.room.TypeConverter
import com.example.inhelper.features.eureka.domain.model.EurekaColor
import com.example.inhelper.features.eureka.domain.model.EurekaSetName
import com.google.gson.Gson

class EurekaTypeConverters {
    @TypeConverter
    fun fromEurekaSetName(value: EurekaSetName): String {
        return value.name
    }

    @TypeConverter
    fun toEurekaSetName(value: String): EurekaSetName {
        return EurekaSetName.valueOf(value)
    }

    @TypeConverter
    fun fromEurekaColor(value: EurekaColor): String {
        return value.name
    }

    @TypeConverter
    fun toEurekaColor(value: String): EurekaColor {
        return EurekaColor.valueOf(value)
    }

    @TypeConverter
    fun fromBooleanArray(value: BooleanArray): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toBooleanArray(value: String): BooleanArray {
        return Gson().fromJson(value, BooleanArray::class.java)
    }

    companion object {
        fun getDrawableRes(context: Context, resName: String?): Int {
            return if (resName.isNullOrEmpty()) {
                0
            } else {
                context.resources.getIdentifier(resName, "drawable", context.packageName)
            }
        }
    }
}
