package com.benyq.sodaplanet.transaction.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.benyq.sodaplanet.base.ext.load
import com.benyq.sodaplanet.transaction.databinding.ViewConsumeBinding
import com.benyq.sodaplanet.transaction.databinding.ViewPayPanelBinding
import com.google.android.material.imageview.ShapeableImageView
import com.orhanobut.logger.Logger

/**
 *
 * @author benyq
 * @date 2022/10/14
 * @email 1520063035@qq.com
 *
 */
class ConsumeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr)  {

    private val binding: ViewConsumeBinding

    init {
        binding = ViewConsumeBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun setBGColor(@ColorRes bgColor: Int) {
        if (bgColor == 0) {
            return
        }
        binding.ivBG.setBackgroundColor(ContextCompat.getColor(context, bgColor))
    }

    fun setDrawable(@DrawableRes resId: Int) {
        binding.ivConsume.load(resId)
    }
}