package com.bjelor.tempvisualizer.ui

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
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entriesOf

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

        val pairs = readings.map {
            Pair(
                it.timestamp.dayOfYear,
                it.pressure.systolic
            )
        }.toTypedArray()
        val entries = entriesOf(*pairs)

        val chartEntryModelProducer1 = ChartEntryModelProducer(entries)

        val lineChart = lineChart()

        Chart(
            chart = remember { lineChart },
            chartModelProducer = chartEntryModelProducer1,
            startAxis = rememberStartAxis(),
            bottomAxis = rememberBottomAxis(),
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