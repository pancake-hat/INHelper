package com.example.inhelper.di

import android.content.Context
import com.example.inhelper.data.EurekaDatabase
import com.example.inhelper.data.EurekaSetDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideEurekaDatabase(@ApplicationContext context: Context): EurekaDatabase {
        return EurekaDatabase.getInstance(context)
    }

    @Provides
    fun provideEurekaSetDao(appDatabase: EurekaDatabase): EurekaSetDao {
        return appDatabase.eurekaSetDao()
    }
}
