package com.benyq.sodaplanet.transaction.ui.add

import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.benyq.sodaplanet.base.base.BaseFragment
import com.benyq.sodaplanet.base.ext.toast
import com.benyq.sodaplanet.base.room.entity.TransactionRecord
import com.benyq.sodaplanet.transaction.R
import com.benyq.sodaplanet.transaction.data.ConsumeType
import com.benyq.sodaplanet.transaction.data.PaidType
import com.benyq.sodaplanet.transaction.data.TransactionIntentExtra
import com.benyq.sodaplanet.transaction.databinding.FragmentTransactionAddRecordBinding
import com.benyq.sodaplanet.transaction.widget.PayPanelView
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author benyq
 * @date 2022/10/10
 * @email 1520063035@qq.com
 * add and edit
 */
class TransactionAddRecordFragment : BaseFragment<FragmentTransactionAddRecordBinding>() {

    private val vm by viewModels<TransactionAddRecordViewModel>()

    private val currentCalendar = Calendar.getInstance().apply {
        firstDayOfWeek = Calendar.MONDAY
    }

    private var currentConsumeType = ConsumeType.singleFood

    private lateinit var paidTypeDialog: BottomPaidTypeDialog

    private var record: TransactionRecord? = null

    private val timePickerView: TimePickerView by lazy {
        val selectedDate = Calendar.getInstance()
        val startDate: Calendar = Calendar.getInstance()
        startDate.set(startDate.get(Calendar.YEAR), 0, 1)
        val endDate: Calendar = Calendar.getInstance()

        TimePickerBuilder(requireActivity()) { date, v ->
            currentCalendar.time = date
            binding.payPanelView.setDate(currentCalendar.timeInMillis)
        }.setLayoutRes(R.layout.pickerview_custom_time) {
            it.findViewById<TextView>(R.id.ivConfirm).setOnClickListener {
                timePickerView.returnData()
                timePickerView.dismiss()
            }
        }.setRangDate(startDate, endDate)
            .setLabel("年", "月", "日", "时", "分", "秒")
            .setDate(selectedDate)
            .setType(booleanArrayOf(false, true, true, false, false, false)).build()
    }

    override fun provideViewBinding() = FragmentTransactionAddRecordBinding.inflate(layoutInflater)

    override fun onFragmentViewCreated(view: View) {
        val consumeTypes = ConsumeType.consumeTypes()
        record = arguments?.getParcelable(TransactionIntentExtra.transactionRecord)
        record?.let {
            binding.payPanelView.setDefaultData(it.amount, it.paidType, it.note)
            binding.payPanelView.setDate(it.createTime)
            consumeTypes.forEach { consume ->
                consume.selected = consume.code == it.consumeType
            }
        }

        binding.rvConsume.grid(4).setup {
            var oldPosition = 0
            addType<ConsumeType>(R.layout.item_consume_type)

            R.id.item.onClick {
                getModel<ConsumeType>(oldPosition).selected = false
                getModel<ConsumeType>(layoutPosition).selected = true
                notifyItemChanged(oldPosition)
                notifyItemChanged(layoutPosition)
                oldPosition = layoutPosition

                currentConsumeType = getModel()
            }
        }.models = consumeTypes

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.payPanelView.itemListener = object : PayPanelView.PayPanelClickListener {
            override fun onClickDate() {
                timePickerView.show()
            }

            override fun onClickDone(amount: String, paidType: PaidType, note: String) {
                if (record == null) {
                    vm.addTransactionRecord(amount, currentConsumeType, paidType, note)
                } else {
                    record?.let {
                        it.paidType = paidType.code
                        it.consumeType = currentConsumeType.code
                        it.note = note
                        it.createTime = currentCalendar.timeInMillis
                    }
                    vm.updateTransactionRecord(record!!, amount)
                }
            }

            override fun onClickWallet() {
                paidTypeDialog.show()
            }
        }

        paidTypeDialog = BottomPaidTypeDialog(requireActivity()) {
            binding.payPanelView.paidType = it
        }
        paidTypeDialog.create()

        injectObserver()
    }

    private fun injectObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.addRecordResult.collect {
                    if (it) {
                        findNavController().navigateUp()
                    } else {
                        toast("数据添加失败")
                    }
                }
            }
        }
    }

}