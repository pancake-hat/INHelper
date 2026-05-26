package com.example.inhelper.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.inhelper.features.eureka.domain.model.EurekaSetName

@Entity(tableName = "eureka_sets")
data class EurekaObtained(
    @PrimaryKey
    @ColumnInfo(name = "set_name")
    val eurekaName: EurekaSetName,

    val headObtained: BooleanArray = BooleanArray(5) { false },
    val handsObtained: BooleanArray = BooleanArray(5) { false },
    val feetObtained: BooleanArray = BooleanArray(5) { false },
) {
    override fun toString(): String {
        return eurekaName.name
    }

    fun totalObtainedCount(): Int {
        return headObtained.count { it } + handsObtained.count { it } + feetObtained.count { it }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EurekaObtained

        if (eurekaName != other.eurekaName) return false
        if (!headObtained.contentEquals(other.headObtained)) return false
        if (!handsObtained.contentEquals(other.handsObtained)) return false
        if (!feetObtained.contentEquals(other.feetObtained)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = eurekaName.hashCode()
        result = 31 * result + headObtained.contentHashCode()
        result = 31 * result + handsObtained.contentHashCode()
        result = 31 * result + feetObtained.contentHashCode()
        return result
    }
}
