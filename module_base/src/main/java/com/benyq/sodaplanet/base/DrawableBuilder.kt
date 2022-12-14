package com.benyq.sodaplanet.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.OVAL
import android.graphics.drawable.GradientDrawable.RECTANGLE
import androidx.annotation.ColorInt
import com.benyq.sodaplanet.base.ext.dp


/**
 * @author benyq
 * @time 2020/3/2
 * @e-mail 1520063035@qq.com
 * @note
 */
class DrawableBuilder {
    //默认线条粗细 1dp
    private val defaultLineWidth = 1
    private val defaultLineColor: Int = Color.parseColor("#e9e9e9")

    private val defaultCornerRadius = 2
    private var defaultCornerRadii: FloatArray ? = null
    //椭圆形圆角
    private val defaultRoundCornerRadius = 100

    //默认虚线条单位长度 6dp
    private val defaultDashWidth = 6
    //默认虚线条之间的间距 2dp
    private val defaultDashGap = 2

    private var shape = RECTANGLE
    private var circleRadius = 50f
    private var lineWidth = 0
    @ColorInt
    private var lineColor: Int = Color.TRANSPARENT
    private var cornerRadius: Float
    /**
     * 背景颜色，默认透明
     */
    @ColorInt
    private var bkColor: Int = Color.TRANSPARENT
    /**
     * 虚线边框每个单元的长度
     */
    private var dashWidth = 0f
    /**
     * 虚线边框每个单元之间的间距
     */
    private var dashGap = 0f

   init {
       cornerRadius = 2f.dp
   }

    fun build(): Drawable {
        val bg = GradientDrawable()
        bg.shape = shape
        bg.setStroke(lineWidth, lineColor, dashWidth, dashGap)
        bg.cornerRadius = cornerRadius
        defaultCornerRadii?.run {
            bg.cornerRadii = this
        }
        if (shape == OVAL) {
            bg.setSize(circleRadius.toInt(), circleRadius.toInt())
        }
        bg.setColor(bkColor)
        return bg
    }

    fun shape(shape: Int): DrawableBuilder {
        this.shape = shape
        return this
    }

    /**
     * 设置圆半径时，默认设置shape
     */
    fun circleRadius(radius: Float) : DrawableBuilder {
        this.shape = OVAL
        this.circleRadius = radius
        return this
    }

    /**
     * 默认生成一个 1 dp 939393 的园线
     * @return
     */
    fun line(): DrawableBuilder {
        return line(defaultLineWidth, defaultLineColor)
    }

    /**
     * 构造线框
     * @param width
     * @param color
     * @return
     */
    fun line(width: Int, color: Int): DrawableBuilder {
        return lineWidth(width).lineColor(color)
    }

    fun line(width: Int, color: String): DrawableBuilder {
        return lineWidth(width).lineColor(color)
    }

    /**
     * 设置边框线条粗细 直接传入具体数值
     *
     * @return
     */
    fun lineWidth(lineWidth: Int): DrawableBuilder {
        this.lineWidth = lineWidth.dp.toInt()
        return this
    }

    fun lineColor(lineColor: Int): DrawableBuilder {
        this.lineColor = lineColor
        return this
    }

    fun lineColor(lineColor: String): DrawableBuilder {
        require(lineColor[0] == '#') { "color value must be start with # like #000000" }
        return lineColor(Color.parseColor(lineColor))
    }

    fun cornerRadii(radii: FloatArray) : DrawableBuilder {
        defaultCornerRadii = radii
        return this
    }

    /**
     * 设置圆角度数，直接传入具体数值
     * @param cornerRadius
     * @return
     */
    fun corner(cornerRadius: Float): DrawableBuilder {
        this.cornerRadius = cornerRadius.dp
        return this
    }

    /**
     * 配置默认的圆角度数 为2
     * @return
     */
    fun corner(): DrawableBuilder {
        return corner(defaultCornerRadius.toFloat())
    }

    /**
     * 大圆角
     * @return
     */
    fun roundCorner(): DrawableBuilder {
        return corner(defaultRoundCornerRadius.toFloat())
    }

    fun fill(@ColorInt bkColor: Int): DrawableBuilder {
        this.bkColor = bkColor
        return this
    }

    fun fill(bkColor: String): DrawableBuilder {
        require(bkColor[0] == '#') { "color value must be start with # like #000000" }
        return fill(Color.parseColor(bkColor))
    }

    /**
     * 生成一个默认的虚线 shape
     * @return
     */
    fun dash(): DrawableBuilder {
        return dashWidth(defaultDashWidth.toFloat()).dashGap(defaultDashGap.toFloat())
    }

    fun dashWidth(dashWidth: Float): DrawableBuilder {
        this.dashWidth = dashWidth.dp
        return this
    }

    fun dashGap(dashGap: Float): DrawableBuilder {
        this.dashGap = dashGap.dp
        return this
    }
}