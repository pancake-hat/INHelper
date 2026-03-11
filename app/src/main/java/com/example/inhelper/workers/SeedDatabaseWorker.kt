package com.example.inhelper.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.inhelper.data.EurekaDatabase
import com.example.inhelper.data.EurekaSet
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class SeedDatabaseWorker(
    context: Context,
    params: WorkerParameters
    ): CoroutineWorker(context, params) {
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val filename = inputData.getString(KEY_SETS_FILENAME)
            if (filename != null) {
                applicationContext.assets.open(filename).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->
                        val eurekaSetType = object : TypeToken<List<EurekaSet>>() {}.type
                        val eurekaSetList: List<EurekaSet> = Gson().fromJson(jsonReader, eurekaSetType)

                        val database = EurekaDatabase.getInstance(applicationContext)
                        database.eurekaSetDao().upsertAll(eurekaSetList)

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