package com.example.inhelper.data

import com.example.inhelper.utils.EurekaSetName
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EurekaSetRepository @Inject constructor(
    private val eurekaSetDao: EurekaSetDao
) {
    fun getEurekaSets(): Flow<List<EurekaSet>> = eurekaSetDao.getEurekaSets()
    fun getEurekaSet(setName: EurekaSetName): Flow<EurekaSet> = eurekaSetDao.getSet(setName)

    suspend fun updateEurekaSet(eurekaSet: EurekaSet) {
        eurekaSetDao.upsert(eurekaSet)
    }

    suspend fun updateEurekaSets(eurekaSets: List<EurekaSet>) {
        eurekaSetDao.upsertAll(eurekaSets)
    }

    companion object {
        @Volatile private var instance: EurekaSetRepository? = null

        fun getInstance(eurekaSetDao: EurekaSetDao) =
            instance ?: synchronized(this) {
                instance ?: EurekaSetRepository(eurekaSetDao)
                    .also { instance = it }
            }
    }
}
