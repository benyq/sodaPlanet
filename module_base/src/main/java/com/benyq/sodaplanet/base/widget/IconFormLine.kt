package com.benyq.sodaplanet.base.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.benyq.sodaplanet.base.R
import com.benyq.sodaplanet.base.databinding.ViewIconFormLineBinding
import com.benyq.sodaplanet.base.ext.gone

/**
 * @author benyq
 * @emil 1520063035@qq.com
 * create at 2019/11/11
 * description:
 */
class IconFormLine @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {

    private val imgResNone = 0

    private var title: String? = ""
    private var content: String? = ""
    private var showDivide = true
    private var showLineArrow = true

    private val lineTitleColor: Int
    private val lineContentColor: Int
    private val formIconBg: Drawable?

    private val imgRes: Int

    private var hint: String? = ""

    private var binding: ViewIconFormLineBinding = ViewIconFormLineBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        orientation = VERTICAL
        isClickable = true

        val array = getContext().obtainStyledAttributes(
            attrs,
            R.styleable.IconFormLine
        )
        title = array.getString(R.styleable.IconFormLine_form_title)

        content = array.getString(R.styleable.IconFormLine_form_content)

        showDivide = array.getBoolean(R.styleable.IconFormLine_line_show_divide, true)
        showLineArrow = array.getBoolean(R.styleable.IconFormLine_line_show_arrow, true)

        hint = array.getString(R.styleable.IconFormLine_et_hint)

        lineTitleColor = array.getColor(
            R.styleable.IconFormLine_form_title_color,
            ContextCompat.getColor(context, R.color.color_3C4044)
        )

        formIconBg = array.getDrawable(R.styleable.IconFormLine_form_icon_bg)

        lineContentColor =
            array.getColor(
                R.styleable.IconFormLine_form_content_color,
                ContextCompat.getColor(context, R.color.color_3C4044)
            )

        imgRes = array.getResourceId(R.styleable.IconFormLine_form_icon, imgResNone)

        array.recycle()
        initView()
    }

    private fun initView() {
        binding.tvTitle.text = title
        binding.tvContent.text = content
        binding.tvContent.hint = hint
        binding.viewLine.visibility = if (showDivide) View.VISIBLE else View.GONE
        binding.ivLineArrow.visibility = if (showLineArrow) View.VISIBLE else View.GONE
        binding.tvTitle.setTextColor(lineTitleColor)
        binding.tvContent.setTextColor(lineContentColor)
        if (imgRes != imgResNone) {
            binding.ivImg.background = formIconBg
            binding.ivImg.setImageResource(imgRes)
        } else {
            binding.ivImg.gone()
        }
    }


    fun setTitle(title: String) {
        binding.tvTitle.text = title
    }

    fun setContent(content: String) {
        binding.tvContent.text = content
        binding.tvContent.setTextColor(ContextCompat.getColor(context, R.color.color_3C4044))
    }

    fun setRedContent(content: String) {
        binding.tvContent.text = content
        binding.tvContent.setTextColor(Color.RED)
    }

    fun getContent(): String {
        return binding.tvContent.text.toString().trim()
    }

}