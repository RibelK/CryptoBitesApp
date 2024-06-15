package com.example.crypto.uii

import android.content.Context
import android.widget.LinearLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.example.crypto.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

@Composable
fun LineChartView(context: Context, data: List<Entry>) {
    AndroidView(factory = { ctx ->
        LineChart(ctx).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            axisRight.isEnabled = false
            axisLeft.setDrawGridLines(false)
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            setScaleEnabled(true)
            setDrawGridBackground(false)

            val lineDataSet = LineDataSet(data, "Price")
            lineDataSet.setDrawValues(false)
            lineDataSet.setDrawCircles(false)
            lineDataSet.color = context.getColor(R.color.purple_700)
            lineDataSet.lineWidth = 2f

            this.data = LineData(lineDataSet)
            invalidate()
        }
    }, update = { lineChart ->
        val lineDataSet = LineDataSet(data, "Price")
        lineDataSet.setDrawValues(false)
        lineDataSet.setDrawCircles(false)
        lineDataSet.color = context.getColor(R.color.purple_700)
        lineDataSet.lineWidth = 2f

        lineChart.data = LineData(lineDataSet)
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()
    })
}
