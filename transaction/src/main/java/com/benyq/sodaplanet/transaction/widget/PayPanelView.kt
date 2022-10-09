package com.benyq.sodaplanet.transaction.widget

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.benyq.sodaplanet.base.ext.toNumberDefault
import com.benyq.sodaplanet.base.room.entity.TransactionRecord
import com.benyq.sodaplanet.transaction.R
import com.benyq.sodaplanet.transaction.databinding.ViewPayPanelBinding
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup

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

    private var clickListener: PayPanelClickListener? = null

    private val binding: ViewPayPanelBinding

    var paidType: TransactionRecord.PaidType = TransactionRecord.PaidType.Cash

    init {
        binding = ViewPayPanelBinding.inflate(LayoutInflater.from(context), this, true)
        initView()
    }

    private fun initView() {
        binding.rvDial.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.right = 10
                outRect.left = 10
                outRect.top = 10
                outRect.bottom = 10
            }
        })
        binding.rvDial.grid(4).setup {
            addType<PanelButtonData>(R.layout.item_pay_panel_button)
            onBind {
                findView<TextView>(R.id.tvContent).text = getModel<PanelButtonData>().text
            }
            R.id.item.onClick {
                dialClick(getModel())
            }

        }.models = buildModels()
    }

    private fun dialClick(data: PanelButtonData) {
        var payAmount = binding.tvPayAmount.text.toString()

        fun calculate(text: String): String {
            var sum = 0f
            if (text.contains("+")) {
                val numbers = payAmount.split("+")
                numbers.forEachIndexed { index, s ->
                    sum += s.toNumberDefault(0f)
                }
            }else if (text.contains("-")) {
                val numbers = payAmount.split("-")
                numbers.forEachIndexed { index, s ->
                    if (index == 0) sum = s.toNumberDefault(0f)
                    else sum -= s.toInt()
                }
            }else {
                return text
            }
            return sum.toString()
        }

        when (data.code) {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" -> {
                if (payAmount == "0") payAmount = data.text
                else if (payAmount.matches(Regex("[0-9]*\\.[0-9]{2}"))){
                    //do nothing
                }
                else payAmount += data.text
            }
            "add", "minus" -> {
                val result = calculate(payAmount)
                payAmount = "$result${data.text}"
            }
            "dot" -> {
                if (payAmount.isEmpty()) payAmount = "0" + data.text
                else if (!payAmount.contains(".")) payAmount += data.text
            }
            "delete" -> {
                payAmount = payAmount.substring(0, payAmount.length - 1)
                if (payAmount.isEmpty()) payAmount = "0"
            }
            "date" -> {
                clickListener?.onClickDate()
            }
            "done" -> {
                clickListener?.onClickDone(payAmount, paidType)
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

    data class PanelButtonData(val code: String, val text: String)

    interface PayPanelClickListener {
        fun onClickDate()
        fun onClickDone(amount: String, paidType: TransactionRecord.PaidType)
    }
}