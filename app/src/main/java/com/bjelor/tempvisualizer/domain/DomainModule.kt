package com.bjelor.tempvisualizer.domain

import com.bjelor.tempvisualizer.data.PressureReadingDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent


@Module
@InstallIn(ActivityComponent::class)
object DomainModule {

    @Provides
    fun provideGetPressureReadingsUseCase(
        pressureReadingDao: PressureReadingDao,
    ): GetPressureReadingsUseCase =
        GetPressureReadingsUseCase(pressureReadingDao)

}