package com.example.inhelper.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.inhelper.data.local.dao.EurekaSetDao
import com.example.inhelper.data.local.entities.EurekaSet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class SeedDatabaseWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val eurekaSetDao: EurekaSetDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val filename = inputData.getString(KEY_SETS_FILENAME)
            if (filename != null) {
                applicationContext.assets.open(filename).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val setType = object : TypeToken<List<EurekaSet>>() {}.type
                        val setList: List<EurekaSet> = Gson().fromJson(jsonReader, setType)

                        eurekaSetDao.upsertAll(setList)
                        Result.success()
                    }
                }
            } else {
                Log.e(TAG, "Error seeding database - no valid filename")
                Result.failure()
            }
        } catch (ex: IOException) {
            Log.e(TAG, "Error seeding database - transient IO issue, retrying", ex)
            Result.retry()
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database - permanent failure", ex)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "SeedDatabaseWorker"
        const val SEED_DATABASE_WORKER_NAME = "SeedDatabaseWorker"
        const val KEY_SETS_FILENAME = "EUREKA_SETS_DATA_FILENAME"
    }
}
