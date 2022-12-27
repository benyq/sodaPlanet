package com.benyq.sodaplanet.music.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.view.*
import com.benyq.sodaplanet.base.ext.clamp
import com.benyq.sodaplanet.base.ext.dp
import com.benyq.sodaplanet.base.ext.textHeight
import com.orhanobut.logger.Logger
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 *
 * @author benyq
 * @date 2022/11/8
 * @email 1520063035@qq.com
 * 仿写薄荷健康 体重卷尺控件
 */
class TapeMeasureView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var viewWidth = 0
    private var viewHeight = 0

    //尺子主体高度占比
    private val contentRatio = 0.5f

    //尺子主体起始y坐标占比
    private val topRatio = 0.3f

    private val contentRectF = RectF()

    private val contentPaint = Paint().apply {
        color = Color.parseColor("#EDEDED")
    }


    private val centerDialWidth = 8f
    private val centerDialHeight = 50f
    private val centerDialPaint = Paint().apply {
        strokeWidth = centerDialWidth
        style = Paint.Style.FILL_AND_STROKE
        color = Color.GREEN
    }

    //去小数，20.0 == 200
    private val minScale = 300
    private val maxScale = 3000
    private var currentScale = 600
    private val viewWidthScale = 40
    private val topTextPaint: TextPaint = TextPaint().apply {
        textSize = 16.dp
    }

    private val dialTextPaint: TextPaint = TextPaint().apply {
        textSize = 12.dp
    }

    private val commonDialWidthBig = 4f
    private val commonDialWidthSmall = 3f
    private val commonDialHeightSmall = 25f
    private val commonDialPaint = Paint().apply {
        strokeWidth = commonDialWidthBig
        style = Paint.Style.FILL_AND_STROKE
        color = Color.GRAY
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w
        viewHeight = h

        contentRectF.set(0f, topRatio * h, w.toFloat(), (contentRatio + topRatio) * h)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            //绘制尺子主体
            it.drawRect(contentRectF, contentPaint)
            //绘制标杆上方文字
            val text = "%.1f".format(currentScale / 10f)
            val textWidth = topTextPaint.measureText(text)
            val textHeight = topTextPaint.textHeight
            it.drawText(
                text,
                (viewWidth - textWidth) / 2,
                contentRectF.top - textHeight / 2,
                topTextPaint
            )
            //绘制尺子刻度
            val leftScale = max(minScale, currentScale - viewWidthScale / 2)
            val rightScale = min(maxScale, currentScale + viewWidthScale / 2)

            for (current in leftScale..rightScale) {
                val startX = (current - leftScale).toFloat() * viewWidth / viewWidthScale
                val startY = contentRectF.top
                if (current % 10 == 0) {
                    commonDialPaint.strokeWidth = commonDialWidthBig
                    it.drawLine(startX, startY, startX, startY + centerDialHeight, commonDialPaint)
                    //整数
                    val scaleText = "${current / 10}"
                    val scaleTextWidth = dialTextPaint.measureText(scaleText)
                    val scaleTextHeight = dialTextPaint.textHeight
                    it.drawText(
                        scaleText,
                        startX - scaleTextWidth / 2,
                        startY + centerDialHeight + scaleTextHeight * 1.5f,
                        dialTextPaint
                    )
                } else {
                    commonDialPaint.strokeWidth = commonDialWidthSmall
                    it.drawLine(
                        startX,
                        startY,
                        startX,
                        startY + commonDialHeightSmall,
                        commonDialPaint
                    )
                }
            }
            //绘制标杆
            it.drawLine(
                viewWidth / 2f,
                contentRectF.top,
                viewWidth / 2f,
                contentRectF.top + centerDialHeight,
                centerDialPaint
            )
        }
    }

    private val gestureDetectorListener = object : GestureDetector.SimpleOnGestureListener() {

        private var distance = 0f
        private var current = 0
        private var animator: ObjectAnimator? = null

        override fun onDown(e: MotionEvent): Boolean {
            animator?.cancel()
            distance = 0f
            current = currentScale
            return true
        }

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {

            Logger.d("gestureDetector onScroll")

            distance += distanceX
            val ratio = distance / viewWidth
            val diff = ratio * viewWidthScale
            setCurrentScale((current + diff) / 10)
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {

            Logger.d("gestureDetector onFling: ${abs(velocityTracker.xVelocity)}")

            if (abs(velocityTracker.xVelocity) < ViewConfiguration.getMaximumFlingVelocity() / 2) {
                return true
            }

            val ratio = (e1.rawX - e2.rawX) / viewWidth
            val diff = ratio * viewWidthScale * 5

            animator = ObjectAnimator.ofFloat(
                this@TapeMeasureView,
                "currentScale",
                current / 10f,
                (current + diff) / 10
            ).apply {
                duration = 500
                start()
            }
            return true
        }
    }

    private val gestureDetector = GestureDetector(context, gestureDetectorListener)

    private lateinit var velocityTracker: VelocityTracker

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (!this::velocityTracker.isInitialized) {
            velocityTracker = VelocityTracker.obtain()
        }
        velocityTracker.addMovement(event)

        if (event?.action == MotionEvent.ACTION_UP) {
            velocityTracker.computeCurrentVelocity(1000)
        }

        return if (event != null) gestureDetector.onTouchEvent(event) else super.onTouchEvent(null)
    }


    fun setCurrentScale(scale: Float) {
        Logger.d("setCurrentScale: $scale")
        currentScale = (scale * 10).toInt().clamp(320, 2980)
        invalidate()
    }
}