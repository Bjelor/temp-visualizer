package com.bjelor.tempvisualizer.ui

import android.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bjelor.tempvisualizer.domain.Patient
import com.bjelor.tempvisualizer.domain.Reading
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.column.ColumnChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider.Companion.fixed
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entriesOf
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PressureChart(
    viewModel: PressureChartViewModel = viewModel(),
) {
    val readings by viewModel.readings.collectAsState()
    val selectedPatient by viewModel.selectedPatient.collectAsState()

    PressureChartContent(
        readings,
        selectedPatient,
        viewModel::onPatientSelected
    )
}

@Composable
private fun PressureChartContent(
    readings: List<Reading>,
    selectedPatient: Patient,
    onPatientSelected: (Patient) -> Unit,
) {
    Column {

        Row(modifier = Modifier.fillMaxWidth()) {
            PatientRadioButton(
                modifier = Modifier.weight(1f),
                patient = Patient.Stefan,
                selectedPatient = selectedPatient,
                onPatientSelected = onPatientSelected,
            )
            PatientRadioButton(
                modifier = Modifier.weight(1f),
                patient = Patient.Elena,
                selectedPatient = selectedPatient,
                onPatientSelected = onPatientSelected,
            )
        }

        val dates = readings.associate { reading ->
            reading.timestamp.toLocalDate().toEpochDay()
                .toFloat() to reading.timestamp.toLocalDate()
        }
        val diastolicEntries = readings.associate { reading ->
            reading.timestamp.toLocalDate().toEpochDay() to reading.pressure.diastolic
        }
        val systolicEntries = readings.associate { reading ->
            val key = reading.timestamp.toLocalDate().toEpochDay()
            key to (reading.pressure.systolic - (diastolicEntries[key] ?: 0))
        }
        val chartEntryModelProducer1 = ChartEntryModelProducer(
            listOf(
                entriesOf(*diastolicEntries.toList().toTypedArray()),
                entriesOf(*systolicEntries.toList().toTypedArray()),
            )
        )
        val dateAxisFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
            (dates[value] ?: LocalDate.ofEpochDay(value.toLong()))
                .format(DateTimeFormatter.ofPattern("d MMM"))
        }

        val columns: List<LineComponent> = listOf(
            LineComponent(Color.TRANSPARENT),
            LineComponent(
                color = Color.BLACK,
                shape = Shapes.roundedCornerShape(50),
                thicknessDp = 4.dp.value,
            ),
        )
        val columnChart = columnChart(
            columns = columns,
            mergeMode = ColumnChart.MergeMode.Stack,
            axisValuesOverrider = fixed(minY = 30f, maxY = 180f)
        )

        Chart(
            modifier = Modifier.padding(16.dp),
            chart = remember { columnChart },
            chartModelProducer = chartEntryModelProducer1,
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(
                valueFormatter = dateAxisFormatter,
            ),
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            items(readings) {
                Column {
                    Text(text = it.patientName)
                    Text(text = it.timestamp.toString())
                    Text(text = "${it.pressure.systolic} ${it.pressure.diastolic} ${it.pressure.pulse}")
                }
            }
        }

    }
}

@Composable
private fun PatientRadioButton(
    patient: Patient,
    selectedPatient: Patient,
    onPatientSelected: (Patient) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable { onPatientSelected(patient) }
            .padding(16.dp),
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
            text = patient.name,
            style = MaterialTheme.typography.bodyMedium
        )
        RadioButton(
            selected = patient == selectedPatient,
            onClick = null,
        )
    }
}

@Preview
@Composable
private fun PressureChartContentPreview() {
    PressureChartContent(
        emptyList(),
        Patient.Stefan
    ) {}
}