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
import com.example.inhelper.data.local.dao.EurekaObtainedDao
import com.example.inhelper.data.local.entities.EurekaObtained
import com.example.inhelper.utils.EUREKA_DATABASE_NAME
import com.example.inhelper.workers.SeedDatabaseWorker
import com.example.inhelper.workers.SeedDatabaseWorker.Companion.SEED_DATABASE_WORKER_NAME

@Database(entities = [EurekaObtained::class], version = 2, exportSchema = false)
@TypeConverters(EurekaTypeConverters::class)
abstract class EurekaObtainedDatabase: RoomDatabase() {
    abstract fun dao(): EurekaObtainedDao

    companion object {
        private const val TAG = "EurekaDatabase"

        @Volatile
        private var instance: EurekaObtainedDatabase? = null

        fun getInstance(context: Context): EurekaObtainedDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): EurekaObtainedDatabase {
            return Room.databaseBuilder(context, EurekaObtainedDatabase::class.java, EUREKA_DATABASE_NAME)
                .fallbackToDestructiveMigration()
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
