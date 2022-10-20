package com.benyq.sodaplanet.transaction.widget

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.benyq.sodaplanet.base.ext.clamp
import com.benyq.sodaplanet.base.ext.dp
import com.benyq.sodaplanet.base.ext.textHeight
import com.orhanobut.logger.Logger
import kotlin.math.max

/**
 *
 * @author benyq
 * @date 2022/10/18
 * @email 1520063035@qq.com
 * 线段图表
 */
class LineChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var dateType = DateType.WEEK

    private var viewWidth = 0
    private var viewHeight = 0

    private var originX = 0f
    private var originY = 0f

    private var drawWidth = 0f
    private var drawHeight = 0f

    private val linePaint: Paint = Paint()
    private val circlePaint: Paint = Paint()
    private val dashPaint: Paint = Paint()
    private val textPaint: TextPaint = TextPaint()

    private var _data: List<Entity> = listOf()
    //内部Padding
    private val innerPadding = 10f

    private val lastPoint = PointF(0f, 0f)

    private val circleData = mutableListOf<CircleData>()
    private val lineData = mutableListOf<Triple<PointF, PointF, Boolean>>()
    private val bottomText = mutableListOf<Pair<PointF, String>>()

    private var textHeight = 0f
    private val darkGray = Color.parseColor("#FFEDEDED")
    private var internalWidth = 0f

    //包含两个坐标点，第一个是 金额的， 第二个是虚线的起点
    private var amountText: Triple<PointF, PointF, String>? = null

    init {
        linePaint.style = Paint.Style.FILL_AND_STROKE
        linePaint.color = Color.BLACK
        linePaint.strokeWidth = 4f
        linePaint.isAntiAlias = true

        circlePaint.style = Paint.Style.FILL_AND_STROKE
        circlePaint.strokeWidth = 4f
        circlePaint.color = Color.BLACK
        circlePaint.isAntiAlias = true

        dashPaint.style = Paint.Style.FILL_AND_STROKE
        dashPaint.pathEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
        dashPaint.color = Color.BLACK
        dashPaint.strokeWidth = 4f
        dashPaint.isAntiAlias = true

        textPaint.style = Paint.Style.FILL_AND_STROKE
        textPaint.color = Color.BLACK
        textPaint.textSize = 12.dp
        textPaint.isAntiAlias = true

        textHeight = textPaint.textHeight
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {

            lineData.forEach { triple ->
                linePaint.color = if (triple.third) darkGray else Color.BLACK
                drawLine(it, triple.first.x, triple.first.y, triple.second.x, triple.second.y, false)
            }
            circleData.forEach { data ->
                drawCircle(it, data.pointF.x, data.pointF.y, 10f, data.checked)
            }
            bottomText.forEach { pair->
                it.drawText(pair.second, pair.first.x, pair.first.y, textPaint)
            }
            amountText?.let { triple ->
                it.drawText(triple.third, triple.first.x, triple.first.y, textPaint)
                drawLine(it, triple.second.x, triple.second.y, triple.second.x, drawHeight + originY, true)
            }
        }
    }

    private var touchX = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.x
            }

            MotionEvent.ACTION_UP -> {
                //开始判断，落在哪一个点
                circleData.forEachIndexed { index, data->
                    data.checked = false
                    if (touchX >= data.pointF.x - internalWidth && touchX <= data.pointF.x + internalWidth) {
                        data.checked = true
                        val text = "%.2f".format(_data[index].value)
                        val textWidth = textPaint.measureText(text)

                        //判断边界
                        val x = (data.pointF.x - textWidth / 2).clamp(originX, originX + drawWidth - textWidth)
                        val point = PointF(x, data.pointF.y - textHeight)
                        amountText = Triple(point, data.pointF, text)
                    }
                }
                invalidate()
            }
        }
        return true
    }

    fun setData(data: List<Entity>, dateType: DateType) {
        this.dateType = dateType
        _data = data
        calculateContent()
        invalidate()
    }

    //这个方法里面所有的内容都是左下角为原点
    private fun calculateContent() {

        circleData.clear()
        lineData.clear()
        bottomText.clear()
        amountText = null
        if (_data.isEmpty()) return

        originX = paddingStart + innerPadding
        originY = paddingTop + innerPadding

        drawWidth = viewWidth - paddingEnd - paddingStart - innerPadding * 2
        drawHeight = viewHeight - paddingTop - paddingBottom - innerPadding * 2 - textHeight

        val lengthY = (_data.maxOf { data -> data.value } - _data.minOf { data -> data.value }) * 1.5f

        internalWidth = drawWidth / (_data.size * 2)
        var startX = internalWidth + originX

        lastPoint.set(originX, drawHeight + originY)

        lineData.add(Triple(PointF(originX,drawHeight + originY),PointF(originX + drawWidth, drawHeight + originY), true))

        _data.forEachIndexed { index, entity ->
            val y = drawHeight - ((entity.value / lengthY * 100).toInt() / 5) * 0.05f * drawHeight + originY

            circleData.add(CircleData(PointF(startX, y), false))

            val textWidth = textPaint.measureText(entity.key)
            val textY = originY + drawHeight + textHeight
            if (dateType == DateType.MONTH) {
                if ((index + 1) % 5 == 0) {
                    bottomText.add(Pair(PointF(startX - textWidth / 2, textY), entity.key))
                }
            }else {
                bottomText.add(Pair(PointF(startX - textWidth / 2, textY), entity.key))
            }

            lineData.add(Triple(PointF(lastPoint.x, lastPoint.y),PointF(startX, y), false))

            lastPoint.set(startX, y)
            startX += internalWidth * 2
        }
        lineData.add(Triple(PointF(lastPoint.x, lastPoint.y),PointF(startX, drawHeight + originY), false))

    }

    private fun drawCircle(canvas: Canvas, cx: Float, cy: Float, radius: Float, fill: Boolean) {
        if (fill) {
            circlePaint.color = Color.BLACK
            circlePaint.style = Paint.Style.FILL_AND_STROKE
            canvas.drawCircle(cx, cy, radius, circlePaint)
        }else {
            //画两个圆，覆盖线
            circlePaint.color = Color.BLACK
            circlePaint.style = Paint.Style.FILL_AND_STROKE
            canvas.drawCircle(cx, cy, radius, circlePaint)

            circlePaint.style = Paint.Style.FILL_AND_STROKE
            circlePaint.color = Color.WHITE
            canvas.drawCircle(cx, cy, radius - circlePaint.strokeWidth, circlePaint)
        }
    }

    private fun drawLine(canvas: Canvas, startX: Float, startY: Float, stopX: Float, stopY: Float, dash: Boolean) {
        if (dash) {
            canvas.drawLine(startX, startY, stopX, stopY, dashPaint)
        }else {
            canvas.drawLine(startX, startY, stopX, stopY, linePaint)
        }
    }

    data class CircleData(var pointF: PointF, var checked: Boolean)

    data class Entity(val key: String, val value: Float)

    enum class DateType {
        WEEK, MONTH, YEAR
    }
}

