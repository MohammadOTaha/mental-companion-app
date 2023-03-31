package com.omar.mentalcompanion.di

import android.app.Application
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.omar.mentalcompanion.AppViewModel
import com.omar.mentalcompanion.data.tracked_data.LocationLiveData
import com.omar.mentalcompanion.data.tracked_data.UsageStatsData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUsageStatsData(@ApplicationContext context: Context): UsageStatsData {
        return UsageStatsData(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideLocationLiveData(@ApplicationContext context: Context): LocationLiveData {
        return LocationLiveData(context)
    }

    @Provides
    @Singleton
    fun provideAppViewModel(app: Application, usageStatsData: UsageStatsData, locationLiveData: LocationLiveData): AppViewModel {
        return AppViewModel(app, usageStatsData, locationLiveData)
    }
}