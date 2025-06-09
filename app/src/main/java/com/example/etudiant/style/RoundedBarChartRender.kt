package com.example.etudiant.style

import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class RoundedBarChartRenderer(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val radius = 30f

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val trans = mChart.getTransformer(dataSet.axisDependency)

        val buffer = mBarBuffers[index]
        buffer.setPhases(mAnimator.phaseX, mAnimator.phaseY)
        buffer.setDataSet(index)
        buffer.setInverted(mChart.isInverted(dataSet.axisDependency))
        buffer.setBarWidth(mChart.barData.barWidth)
        buffer.feed(dataSet)

        trans.pointValuesToPixel(buffer.buffer)

        val isInverted = mChart.isInverted(dataSet.axisDependency)

        mRenderPaint.color = dataSet.color

        var j = 0
        while (j < buffer.size()) {
            val left = buffer.buffer[j]
            val top = buffer.buffer[j + 1]
            val right = buffer.buffer[j + 2]
            val bottom = buffer.buffer[j + 3]

            val barRect = RectF(left, top, right, bottom)

            val path = Path()
            if (isInverted) {
                path.addRoundRect(barRect, floatArrayOf(
                    0f, 0f,
                    0f, 0f,
                    radius, radius,
                    radius, radius
                ), Path.Direction.CW)
            } else {
                path.addRoundRect(barRect, floatArrayOf(
                    radius, radius,
                    radius, radius,
                    0f, 0f,
                    0f, 0f
                ), Path.Direction.CW)
            }

            c.drawPath(path, mRenderPaint)

            // Optional: draw border if applicable
            if (dataSet.barBorderWidth > 0f) {
                mBarBorderPaint.color = dataSet.barBorderColor
                mBarBorderPaint.strokeWidth = dataSet.barBorderWidth
                c.drawPath(path, mBarBorderPaint)
            }

            j += 4
        }
    }
}
