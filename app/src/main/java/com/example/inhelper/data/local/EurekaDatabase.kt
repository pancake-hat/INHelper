package com.example.inhelper.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.inhelper.data.local.dao.EurekaSetDao
import com.example.inhelper.data.local.entities.EurekaSet
import com.example.inhelper.utils.EUREKA_DATABASE_NAME
import com.example.inhelper.workers.SeedDatabaseWorker
import com.example.inhelper.workers.SeedDatabaseWorker.Companion.SEED_DATABASE_WORKER_NAME

@Database(entities = [EurekaSet::class], version = 1, exportSchema = false)
@TypeConverters(EurekaTypeConverters::class)
abstract class EurekaDatabase: RoomDatabase() {
    abstract fun eurekaSetDao(): EurekaSetDao

    companion object {
        private const val TAG = "EurekaDatabase"

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
                            Log.d(TAG, "Database created, enqueuing seed worker")

                            WorkManager.getInstance(context).enqueueUniqueWork(
                                SEED_DATABASE_WORKER_NAME,
                                ExistingWorkPolicy.REPLACE,
                                SeedDatabaseWorker.buildWorkRequest()
                            )
                        }
                    }
                )
                .build()
        }
    }
}
