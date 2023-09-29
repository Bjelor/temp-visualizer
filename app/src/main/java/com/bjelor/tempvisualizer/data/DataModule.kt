package com.bjelor.tempvisualizer.data

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providePressureDatabase(@ApplicationContext applicationContext: Context): PressureDatabase =
        Room
            .databaseBuilder(
                applicationContext,
                PressureDatabase::class.java, PressureDatabase.NAME
            )
            .build()

    @Provides
    fun providePressureReadingDao(db: PressureDatabase): PressureReadingDao {
        return db.pressureReadingDao()
    }
}