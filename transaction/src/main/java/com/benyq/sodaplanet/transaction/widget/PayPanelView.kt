package com.benyq.sodaplanet.transaction.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.benyq.sodaplanet.base.ext.dp
import com.benyq.sodaplanet.base.ext.textTrim
import com.benyq.sodaplanet.base.ext.toNumberDefault
import com.benyq.sodaplanet.base.room.entity.TransactionRecord
import com.benyq.sodaplanet.transaction.R
import com.benyq.sodaplanet.transaction.data.PaidType
import com.benyq.sodaplanet.transaction.databinding.ViewPayPanelBinding
import com.drake.brv.utils.grid
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.orhanobut.logger.Logger
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author benyq
 * @date 2022/10/8
 * @email 1520063035@qq.com
 * 支付按键 面板
 */
class PayPanelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var itemListener: PayPanelClickListener? = null

    private val binding: ViewPayPanelBinding

    var paidType: PaidType = PaidType.cash
        set(value) {
            field = value
            binding.ivWallet.setImageResource(value.resId)
        }

    init {
        binding = ViewPayPanelBinding.inflate(LayoutInflater.from(context), this, true)
        initView()
    }

    fun setDefaultData(amount: String, paidCode: Int, note: String) {
        binding.tvPayAmount.text = amount
        binding.tvPayNote.setText(note)
        paidType = PaidType.fromCode(paidCode)
    }

    fun setDate(time: Long) {

        var dateText = SimpleDateFormat("MM-dd", Locale.CHINA).format(time)

        val calendarToday = Calendar.getInstance()
        calendarToday.time = Date()

        val currentCalendar = Calendar.getInstance()
        currentCalendar.timeInMillis = time

        if (currentCalendar.get(Calendar.DAY_OF_YEAR) == calendarToday.get(Calendar.DAY_OF_YEAR)) {
            dateText = "今天"
        } else if (currentCalendar.get(Calendar.DAY_OF_YEAR) == calendarToday.get(Calendar.DAY_OF_YEAR) - 1) {
            dateText = "昨天"
        }

        Logger.d("date: $dateText")
        var dateIndex = -1
        binding.rvDial.models?.forEachIndexed { index, any ->
            if ((any as PanelButtonData).code == "date") {
                dateIndex = index
                any.text = dateText
            }
        }
        if (dateIndex != -1) {
            binding.rvDial.adapter?.notifyItemChanged(dateIndex)
        }
    }

    private fun initView() {
        binding.rvDial.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.right = 6.dp.toInt()
                outRect.left = 6.dp.toInt()
                outRect.top = 10
                outRect.bottom = 10
            }
        })
        binding.rvDial.grid(4).setup {
            addType<PanelButtonData>(R.layout.item_pay_panel_button)
            onBind {
                findView<TextView>(R.id.tvContent).text = getModel<PanelButtonData>().text
            }
            R.id.item.onFastClick {
                dialClick(getModel())
            }

        }.models = buildModels()

        binding.ivWallet.setOnClickListener {
            itemListener?.onClickWallet()
        }
    }

    private fun dialClick(data: PanelButtonData) {
        var payAmount = binding.tvPayAmount.text.toString()

        fun calculate(text: String): String {
            var sum = 0f
            if (text.contains("+")) {
                val numbers = payAmount.split("+")
                numbers.forEachIndexed { _, s ->
                    sum += s.toNumberDefault(0f)
                }
            }else if (text.contains("-")) {
                val numbers = payAmount.split("-")
                numbers.forEachIndexed { index, s ->
                    if (index == 0) sum = s.toNumberDefault(0f)
                    else sum -= s.toNumberDefault(0f)
                }
            }else {
                return text
            }
            return sum.toString()
        }

        when (data.code) {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> {
                val lastNumber = payAmount.split("[+\\-]".toRegex()).last()

                if (payAmount == "0") payAmount = data.text
                else if (lastNumber.matches(Regex("[0-9]*\\.[0-9]{2}"))){
                    //do nothing
                }
                else payAmount += data.text
            }
            "add", "minus" -> {
                val result = calculate(payAmount)
                payAmount = "$result${data.text}"
            }
            "dot" -> {
                val lastNumber = payAmount.split("[+\\-]".toRegex()).last()
                if (lastNumber.isEmpty()) payAmount += "0" + data.text
                else if (!lastNumber.contains(".")) payAmount += data.text
            }
            "delete" -> {
                payAmount = payAmount.substring(0, payAmount.length - 1)
                if (payAmount.isEmpty()) payAmount = "0"
            }
            "date" -> {
                itemListener?.onClickDate()
            }
            "done" -> {
                val note = binding.tvPayNote.textTrim()
                itemListener?.onClickDone(calculate(payAmount), paidType, note)
            }
        }
        binding.tvPayAmount.text = payAmount
    }

    private fun buildModels(): List<PanelButtonData> {
        return listOf(
            PanelButtonData("7", "7"),
            PanelButtonData("8", "8"),
            PanelButtonData("9", "9"),
            PanelButtonData("date", "今天"),
            PanelButtonData("4", "4"),
            PanelButtonData("5", "5"),
            PanelButtonData("6", "6"),
            PanelButtonData("add", "+"),
            PanelButtonData("1", "1"),
            PanelButtonData("2", "2"),
            PanelButtonData("3", "3"),
            PanelButtonData("minus", "-"),
            PanelButtonData("dot", "."),
            PanelButtonData("0", "0"),
            PanelButtonData("delete", "删除"),
            PanelButtonData("done", "完成"),
        )
    }

    data class PanelButtonData(val code: String, var text: String)

    interface PayPanelClickListener {
        fun onClickDate()
        fun onClickDone(amount: String, paidType: PaidType, note: String)
        fun onClickWallet()
    }
}