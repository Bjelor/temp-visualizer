package com.bjelor.tempvisualizer.domain

import com.bjelor.tempvisualizer.data.PressureReadingDao
import com.bjelor.tempvisualizer.data.PressureReadingDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class GetPressureReadingsUseCase @Inject constructor(
    private val pressureReadingDao: PressureReadingDao,
) {

    operator fun invoke(patient: Patient): Flow<List<Reading>> =
        pressureReadingDao.getReadingsByName(patient.name)
            .map { list ->
                list.map { dto ->
                    dto.toDomain()
                }
            }

    private fun PressureReadingDto.toDomain(): Reading = Reading(
        patientName = name,
        timestamp = date.parseAsLocalDate().atTime(time.parseAsLocalTime()),
        pressure = Reading.Pressure(
            systolic = systolic,
            diastolic = diastolic,
            pulse = pulse,
        )

    )

    private fun String.parseAsLocalDate() = LocalDate.parse(
        this,
        DateTimeFormatter.ofPattern("d.M.yyyy"),
    )

    private fun String.parseAsLocalTime() = LocalTime.parse(
        this,
        DateTimeFormatter.ofPattern("H:mm")
    )
}