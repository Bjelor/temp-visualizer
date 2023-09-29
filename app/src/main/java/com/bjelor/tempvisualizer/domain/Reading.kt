package com.bjelor.tempvisualizer.domain

import java.time.LocalDateTime

data class Reading(
    val patientName: String,
    val timestamp: LocalDateTime,
    val pressure: Pressure,
) {
    data class Pressure(
        val systolic: Int,
        val diastolic: Int,
        val pulse: Int,
    )
}