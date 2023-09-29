package com.bjelor.tempvisualizer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bjelor.tempvisualizer.domain.GetPressureReadingsUseCase
import com.bjelor.tempvisualizer.domain.Reading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PressureChartViewModel @Inject constructor(
    getPressureReadingsUseCase: GetPressureReadingsUseCase,
) : ViewModel() {

    val readings: StateFlow<List<Reading>> =
        getPressureReadingsUseCase(GetPressureReadingsUseCase.Name.Stefan)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

}