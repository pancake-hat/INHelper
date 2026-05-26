package com.example.inhelper.data.repository

import com.example.inhelper.data.local.dao.EurekaObtainedDao
import com.example.inhelper.data.local.entities.EurekaObtained
import com.example.inhelper.features.eureka.domain.model.EurekaSetName
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EurekaObtainedRepository @Inject constructor(
    private val eurekaObtainedDao: EurekaObtainedDao
) {
    fun getEurekasObtainedList(): Flow<List<EurekaObtained>> = eurekaObtainedDao.getEurekaSets()
    fun getEurekaObtained(setName: EurekaSetName): Flow<EurekaObtained> = eurekaObtainedDao.getSet(setName)

    suspend fun updateEurekaObtained(eurekaObtained: EurekaObtained) {
        eurekaObtainedDao.upsert(eurekaObtained)
    }

    suspend fun updateAllEurekasObtained(eurekaObtained: List<EurekaObtained>) {
        eurekaObtainedDao.upsertAll(eurekaObtained)
    }

    suspend fun getTotalObtainedCount(): Int = eurekaObtainedDao.count()

    companion object {
        @Volatile private var instance: EurekaObtainedRepository? = null

        fun getInstance(eurekaObtainedDao: EurekaObtainedDao) =
            instance ?: synchronized(this) {
                instance ?: EurekaObtainedRepository(eurekaObtainedDao)
                    .also { instance = it }
            }
    }
}
