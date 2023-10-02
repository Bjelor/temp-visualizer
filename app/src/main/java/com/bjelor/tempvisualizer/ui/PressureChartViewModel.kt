package com.bjelor.tempvisualizer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bjelor.tempvisualizer.domain.GetPressureReadingsUseCase
import com.bjelor.tempvisualizer.domain.Patient
import com.bjelor.tempvisualizer.domain.Reading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PressureChartViewModel @Inject constructor(
    getPressureReadingsUseCase: GetPressureReadingsUseCase,
) : ViewModel() {

    private val _selectedPatient: MutableStateFlow<Patient> = MutableStateFlow(Patient.Stefan)
    val selectedPatient: StateFlow<Patient> = _selectedPatient.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val readings: StateFlow<List<Reading>> =
        selectedPatient.flatMapLatest { patient ->
            getPressureReadingsUseCase(patient)
        }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun onPatientSelected(patient: Patient) {
        _selectedPatient.value = patient
    }

}