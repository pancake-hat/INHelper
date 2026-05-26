package com.example.inhelper.di

import android.content.Context
import com.example.inhelper.data.local.EurekaObtainedDatabase
import com.example.inhelper.data.local.dao.EurekaObtainedDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideEurekaObtainedDatabase(@ApplicationContext context: Context): EurekaObtainedDatabase {
        return EurekaObtainedDatabase.getInstance(context)
    }

    @Provides
    fun provideEurekaSetObtainedDao(appDatabase: EurekaObtainedDatabase): EurekaObtainedDao {
        return appDatabase.dao()
    }
}
