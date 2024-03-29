package com.omar.mentalcompanion.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.omar.mentalcompanion.AppViewModel
import com.omar.mentalcompanion.data.data_source.local.RoomDb
import com.omar.mentalcompanion.domain.repositories.ApplicationUsageRepository
import com.omar.mentalcompanion.domain.repositories.LocationRepository
import com.omar.mentalcompanion.domain.repositories.MetaDataRepository
import com.omar.mentalcompanion.domain.services.NotificationSchedulerService
import com.omar.mentalcompanion.domain.services.SyncSchedulerService
import com.omar.mentalcompanion.domain.tracked_data.PhoneCallsLogData
import com.omar.mentalcompanion.domain.tracked_data.UsageStatsData
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
    fun provideRoomDatabase(app: Application): RoomDb {
        return Room.databaseBuilder(
            app,
            RoomDb::class.java,
            RoomDb.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideLocationRepository(db: RoomDb): LocationRepository {
        return LocationRepository(db.locationDao())
    }

    @Provides
    @Singleton
    fun provideApplicationUsageRepository(db: RoomDb): ApplicationUsageRepository {
        return ApplicationUsageRepository(db.applicationUsageDao())
    }

    @Provides
    @Singleton
    fun provideMetaDataRepository(db: RoomDb): MetaDataRepository {
        return MetaDataRepository(db.metaDataDao())
    }

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
    fun provideAppViewModel(app: Application, usageStatsData: UsageStatsData): AppViewModel {
        return AppViewModel(app, usageStatsData)
    }


    @Provides
    @Singleton
    fun provideNotificationSchedulerService(@ApplicationContext context: Context): NotificationSchedulerService {
        return NotificationSchedulerService(context)
    }

    @Provides
    @Singleton
    fun provideSyncSchedulerService(@ApplicationContext context: Context): SyncSchedulerService {
        return SyncSchedulerService(context)
    }

    @Provides
    @Singleton
    fun providePhoneCallsLogData(@ApplicationContext context: Context): PhoneCallsLogData {
        return PhoneCallsLogData(context)
    }
}