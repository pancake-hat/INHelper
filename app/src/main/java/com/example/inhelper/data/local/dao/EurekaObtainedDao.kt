package com.example.inhelper.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Upsert
import com.example.inhelper.data.local.EurekaTypeConverters
import com.example.inhelper.data.local.entities.EurekaObtained
import com.example.inhelper.features.eureka.domain.model.EurekaSetName
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(EurekaTypeConverters::class)
interface EurekaObtainedDao {
    @Upsert
    suspend fun upsertAll(setList: List<EurekaObtained>)

    @Upsert
    suspend fun upsert(eurekaObtained: EurekaObtained)

    @Query("SELECT * FROM eureka_sets ORDER BY set_name ASC")
    fun getEurekaSets(): Flow<List<EurekaObtained>>

    @Query("SELECT * FROM eureka_sets WHERE set_name = :name")
    fun getSet(name: EurekaSetName): Flow<EurekaObtained>

    @Query("SELECT COUNT(*) FROM eureka_sets")
    suspend fun count(): Int
}
