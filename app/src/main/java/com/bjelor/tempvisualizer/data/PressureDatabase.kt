package com.bjelor.tempvisualizer.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(version = 1, entities = [PressureReadingDto::class])
abstract class PressureDatabase: RoomDatabase() {

    abstract fun pressureReadingDao(): PressureReadingDao

    companion object {
        const val NAME = "pressure"
    }
}