package com.bjelor.tempvisualizer.ui

import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.chart.draw.ChartDrawContext
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.extension.half
import kotlin.math.max

class PressureAxisItemPlacer(
    private val stepSize: Float,
    private val stepCount: Int,
) : AxisItemPlacer.Vertical {

    override fun getLabelValues(
        context: ChartDrawContext,
        axisHeight: Float,
        maxLabelHeight: Float,
        position: AxisPosition.Vertical,
    ) = getWidthMeasurementLabelValues(context, axisHeight, maxLabelHeight, position)

    override fun getWidthMeasurementLabelValues(
        context: MeasureContext,
        axisHeight: Float,
        maxLabelHeight: Float,
        position: AxisPosition.Vertical,
    ): List<Float> {
        val labelValues = mutableListOf<Float>()
        repeat(stepCount) {
            labelValues.add(stepSize * (it + 1))
        }
        return labelValues
    }

    override fun getHeightMeasurementLabelValues(
        context: MeasureContext,
        position: AxisPosition.Vertical,
    ): List<Float> {
        val chartValues = context.chartValuesManager.getChartValues(position)
        return listOf(
            chartValues.minY,
            (chartValues.minY + chartValues.maxY).half,
            chartValues.maxY
        )
    }

    override fun getTopVerticalAxisInset(
        verticalLabelPosition: VerticalAxis.VerticalLabelPosition,
        maxLabelHeight: Float,
        maxLineThickness: Float,
    ) = when (verticalLabelPosition) {
        VerticalAxis.VerticalLabelPosition.Top ->
            maxLabelHeight + maxLineThickness.half

        VerticalAxis.VerticalLabelPosition.Center ->
            (max(maxLabelHeight, maxLineThickness) + maxLineThickness).half

        VerticalAxis.VerticalLabelPosition.Bottom -> maxLineThickness
    }

    override fun getBottomVerticalAxisInset(
        verticalLabelPosition: VerticalAxis.VerticalLabelPosition,
        maxLabelHeight: Float,
        maxLineThickness: Float,
    ): Float = when (verticalLabelPosition) {
        VerticalAxis.VerticalLabelPosition.Top -> maxLineThickness
        VerticalAxis.VerticalLabelPosition.Center -> (maxOf(
            maxLabelHeight,
            maxLineThickness
        ) + maxLineThickness).half

        VerticalAxis.VerticalLabelPosition.Bottom -> maxLabelHeight + maxLineThickness.half
    }
}
