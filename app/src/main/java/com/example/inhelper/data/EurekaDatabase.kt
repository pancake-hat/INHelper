package com.example.inhelper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.BackoffPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.example.inhelper.utils.EUREKA_DATABASE_NAME
import com.example.inhelper.utils.EUREKA_SETS_DATA_FILENAME
import com.example.inhelper.workers.SeedDatabaseWorker
import com.example.inhelper.workers.SeedDatabaseWorker.Companion.KEY_SETS_FILENAME
import com.example.inhelper.workers.SeedDatabaseWorker.Companion.SEED_DATABASE_WORKER_NAME
import java.util.concurrent.TimeUnit

@Database(entities = [EurekaSet::class], version = 1, exportSchema = false)
@TypeConverters(EurekaTypeConverters::class)
abstract class EurekaDatabase: RoomDatabase() {
    abstract fun eurekaSetDao(): EurekaSetDao

    companion object {
        @Volatile
        private var instance: EurekaDatabase? = null

        fun getInstance(context: Context): EurekaDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): EurekaDatabase {
            return Room.databaseBuilder(context, EurekaDatabase::class.java, EUREKA_DATABASE_NAME)
                .addCallback(
                    object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<SeedDatabaseWorker>()
                                .setInputData(workDataOf(KEY_SETS_FILENAME to EUREKA_SETS_DATA_FILENAME))
                                .setBackoffCriteria(
                                    BackoffPolicy.EXPONENTIAL,
                                    WorkRequest.MIN_BACKOFF_MILLIS,
                                    TimeUnit.MILLISECONDS
                                )
                                .build()
                            
                            WorkManager.getInstance(context).enqueueUniqueWork(
                                SEED_DATABASE_WORKER_NAME,
                                ExistingWorkPolicy.KEEP,
                                request
                            )
                        }
                    }
                )
                .build()
        }
    }
}
