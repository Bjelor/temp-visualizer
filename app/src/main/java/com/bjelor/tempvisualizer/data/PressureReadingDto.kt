package com.bjelor.tempvisualizer.data

import androidx.room.Entity

@Entity(tableName = "pressure", primaryKeys = ["name", "date", "time"])
data class PressureReadingDto(
    val name: String,
    val date: String,
    val time: String,
    val systolic: Int,
    val diastolic: Int,
    val pulse: Int,
)
