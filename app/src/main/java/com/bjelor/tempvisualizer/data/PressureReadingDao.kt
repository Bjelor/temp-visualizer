package com.bjelor.tempvisualizer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PressureReadingDao {

    @Query("SELECT * FROM pressure")
    fun getAll(): Flow<List<PressureReadingDto>>

    @Query("SELECT * FROM pressure WHERE name IS :name")
    fun getReadingsByName(name: String): Flow<List<PressureReadingDto>>

    @Insert
    suspend fun insertAll(readings: List<PressureReadingDto>)

}