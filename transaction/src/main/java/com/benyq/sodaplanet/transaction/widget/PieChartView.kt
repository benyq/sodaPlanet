package com.benyq.sodaplanet.transaction.widget

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.benyq.sodaplanet.base.ext.dp
import com.benyq.sodaplanet.base.ext.textHeight
import com.orhanobut.logger.Logger
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

/**
 *
 * @author benyq
 * @date 2022/10/19
 * @email 1520063035@qq.com
 *
 */
class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var _data: List<PieData> = listOf()

    private var viewWidth = 0
    private var viewHeight = 0

    private var drawWidth = 0f
    private var drawHeight = 0f

    private var originX = 0f
    private var originY = 0f

    private var arcRectF = RectF()
    private val paint = Paint()
    private val linePaint = Paint()
    private val textPaint = TextPaint()

    private var circleStroke = 50f
    private var radius = 0f
    private val path = Path()
    private var textHeight = 0f


    init {
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        paint.strokeWidth = circleStroke

        linePaint.isAntiAlias = true
        linePaint.style = Paint.Style.STROKE
        linePaint.strokeWidth = 5f
        linePaint.color = Color.BLACK

        textPaint.isAntiAlias = true
        textPaint.color = Color.BLACK
        textPaint.textSize = 12.dp

        textHeight = textPaint.textHeight
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h

        drawWidth = (viewWidth - paddingEnd - paddingStart).toFloat()
        drawHeight = (viewHeight - paddingTop - paddingBottom).toFloat()

        radius = minOf(drawWidth, drawHeight) / 2f
        circleStroke = radius / 2
        paint.strokeWidth = circleStroke

        val xSpace = (viewWidth - radius) / 2
        val ySpace = (viewHeight - radius) / 2

        arcRectF = RectF(
            xSpace,
            ySpace,
            viewWidth - xSpace,
            viewHeight - ySpace
        )

        originX = paddingStart.toFloat()
        originY = paddingTop.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val rx = (arcRectF.left + arcRectF.right) / 2
        val ry = (arcRectF.top + arcRectF.bottom) / 2
        val rL0 = radius - circleStroke
        val rL1 = radius - 2 * circleStroke / 3f
        val rL2 = radius - circleStroke / 2 + 10f


        canvas?.let {

            var startAngle = -90f
            var oldRatio = 0f

            _data.forEach { data ->
                paint.color = data.color
                val swapAngle = 360f * data.ratio
                it.drawArc(arcRectF, startAngle, swapAngle, false, paint)

                val angleTemp = (2 * Math.PI * (data.ratio / 2 + oldRatio)).toFloat()
                path.moveTo(rx + rL1 * sin(angleTemp), ry - rL1 * cos(angleTemp))
                //绘制key
                textPaint.textSize = 14.dp

                var textWidth = textPaint.measureText(data.key)
                var baseLineY = abs(textPaint.ascent() + textPaint.descent()) / 2 //垂直中心

                it.drawText(data.key, rx + rL0 * sin(angleTemp) - textWidth / 2, ry - rL0 * cos(angleTemp) + baseLineY, textPaint)

                path.lineTo(rx + rL2 * sin(angleTemp), ry - rL2 * cos(angleTemp))
                baseLineY = abs(textPaint.ascent() + textPaint.descent()) / 2 //垂直中心
                val textMargin = 10f
                //绘制value
                textPaint.textSize = 12.dp
                if (data.ratio / 2 + oldRatio > 0.5f) {
                    path.lineTo(rx + rL2 * sin(angleTemp) - circleStroke, ry - rL2 * cos(angleTemp))
                    textWidth = textPaint.measureText(data.value)
                    it.drawText(
                        data.value,
                        rx + rL2 * sin(angleTemp) - circleStroke - textWidth - textMargin,
                        ry - rL2 * cos(angleTemp) + baseLineY,
                        textPaint
                    )
                } else {
                    path.lineTo(rx + rL2 * sin(angleTemp) + circleStroke, ry - rL2 * cos(angleTemp))
                    it.drawText(
                        data.value,
                        rx + rL2 * sin(angleTemp) + circleStroke + textMargin,
                        ry - rL2 * cos(angleTemp) + baseLineY,
                        textPaint
                    )
                }

                it.drawPath(path, linePaint)
                startAngle += swapAngle
                oldRatio += data.ratio

            }
        }

    }

    fun setData(data: List<PieData>) {
        var total = 0f
        _data = data.filter {
            if (it.ratio > 0.05f) {
                total += it.ratio
                true
            } else false
        }.sortedByDescending {
            it.ratio
        }
        _data.forEach {
            it.ratio = it.ratio / total
        }
        path.reset()
        invalidate()
    }

    data class PieData(val key: String, val value: String, var ratio: Float, val color: Int)

}