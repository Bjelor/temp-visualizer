package com.bjelor.tempvisualizer.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PressureChart(
    viewModel: PressureChartViewModel = viewModel(),
) {
    val readings by viewModel.readings.collectAsState()

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