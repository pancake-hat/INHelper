package com.example.inhelper.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.inhelper.utils.EurekaColor
import com.example.inhelper.utils.EurekaSetName

// https://infinity-nikki.fandom.com/wiki/Eureka
@Entity(tableName = "eureka_sets")
data class EurekaSet(
    @PrimaryKey
    @ColumnInfo(name = "set_name")
    val setName: EurekaSetName,

    val color1: EurekaColor,
    val color2: EurekaColor,
    val color3: EurekaColor,
    val color4: EurekaColor,
    val color5: EurekaColor = EurekaColor.IRIDESCENT,

    val headRes: String? = null,
    val handsRes: String? = null,
    val feetRes: String? = null,

    val headObtained: BooleanArray = BooleanArray(5) { false },
    val handsObtained: BooleanArray = BooleanArray(5) { false },
    val feetObtained: BooleanArray = BooleanArray(5) { false },
) {
    override fun toString(): String {
        return setName.name
    }

    fun totalObtainedCount(): Int {
        return headObtained.count { it } + handsObtained.count { it } + feetObtained.count { it }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EurekaSet

        if (headRes != other.headRes) return false
        if (handsRes != other.handsRes) return false
        if (feetRes != other.feetRes) return false
        if (setName != other.setName) return false
        if (color1 != other.color1) return false
        if (color2 != other.color2) return false
        if (color3 != other.color3) return false
        if (color4 != other.color4) return false
        if (color5 != other.color5) return false
        if (!headObtained.contentEquals(other.headObtained)) return false
        if (!handsObtained.contentEquals(other.handsObtained)) return false
        if (!feetObtained.contentEquals(other.feetObtained)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = headRes.hashCode()
        result = 31 * result + (handsRes.hashCode())
        result = 31 * result + (feetRes.hashCode())
        result = 31 * result + setName.hashCode()
        result = 31 * result + color1.hashCode()
        result = 31 * result + color2.hashCode()
        result = 31 * result + color3.hashCode()
        result = 31 * result + color4.hashCode()
        result = 31 * result + color5.hashCode()
        result = 31 * result + headObtained.contentHashCode()
        result = 31 * result + handsObtained.contentHashCode()
        result = 31 * result + feetObtained.contentHashCode()
        return result
    }
}
