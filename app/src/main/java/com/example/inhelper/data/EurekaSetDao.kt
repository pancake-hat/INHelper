package com.example.inhelper.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Upsert
import com.example.inhelper.utils.EurekaSetName
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(EurekaTypeConverters::class)
interface EurekaSetDao {
    @Upsert
    suspend fun upsertAll(setList: List<EurekaSet>)

    @Upsert
    suspend fun upsert(eurekaSet: EurekaSet)

    @Query("SELECT * FROM eureka_sets ORDER BY set_name ASC")
    fun getEurekaSets(): Flow<List<EurekaSet>>

    @Query("SELECT * FROM eureka_sets WHERE set_name = :name")
    fun getSet(name: EurekaSetName): Flow<EurekaSet>
}
