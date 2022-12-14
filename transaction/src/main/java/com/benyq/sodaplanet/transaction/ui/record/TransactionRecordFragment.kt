package com.benyq.sodaplanet.transaction.ui.record

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.benyq.sodaplanet.base.ui.BaseFragment
import com.benyq.sodaplanet.base.ext.launchAndRepeatWithViewLifecycle
import com.benyq.sodaplanet.transaction.R
import com.benyq.sodaplanet.transaction.data.TransactionGroupRecordData
import com.benyq.sodaplanet.transaction.data.TransactionIntentExtra
import com.benyq.sodaplanet.transaction.data.TransactionRecordData
import com.benyq.sodaplanet.transaction.databinding.FragmentTransactionRecordBinding
import com.benyq.sodaplanet.transaction.ui.detail.TransactionRecordDetailActivity
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.orhanobut.logger.Logger
import java.util.*

/**
 *
 * @author benyq
 * @date 2022/10/10
 * @email 1520063035@qq.com
 *
 */
@SuppressLint("SetTextI18n")
class TransactionRecordFragment : BaseFragment<FragmentTransactionRecordBinding>() {

    private val vm by viewModels<TransactionRecordViewModel>()
    private val currentCalendar = Calendar.getInstance().apply {
        firstDayOfWeek = Calendar.MONDAY
    }

    private val timePickerView: TimePickerView by lazy {
        val selectedDate = Calendar.getInstance()
        val startDate: Calendar = Calendar.getInstance()
        startDate.set(2021, 0, 1)
        val endDate: Calendar = Calendar.getInstance()

        TimePickerBuilder(requireActivity()) { date, v ->
            currentCalendar.time = date
            queryRecords()

            val calendar = Calendar.getInstance()
            calendar.firstDayOfWeek = Calendar.MONDAY
            val year = calendar.get(Calendar.YEAR)
            calendar.time = date
            val currentYear = calendar.get(Calendar.YEAR)
            val currentMonth = calendar.get(Calendar.MONTH)

            binding.tvDate.text = if (currentYear != year) "$currentYear ${currentMonth + 1}?????????" else "${currentMonth + 1}?????????"

        }.setLayoutRes(R.layout.pickerview_custom_time){
            it.findViewById<TextView>(R.id.ivConfirm).setOnClickListener {
                timePickerView.returnData()
                timePickerView.dismiss()
            }
        }.setRangDate(startDate, endDate)
            .setLabel("???","???","???","???","???","???")
            .setDate(selectedDate)
            .setType(booleanArrayOf(true, true, false, false, false, false)).build()
    }

    override fun provideViewBinding() = FragmentTransactionRecordBinding.inflate(layoutInflater)

    override fun onFragmentViewCreated(view: View) {

        binding.rvTransactionRecord.linear().setup {
            addType<TransactionGroupRecordData>(R.layout.item_transaction_group_record)
            addType<TransactionRecordData>(R.layout.item_transaction_record)

            R.id.item.onClick {
                val data = getModel<TransactionRecordData>()
                startActivity(Intent(requireActivity(), TransactionRecordDetailActivity::class.java).apply {
                    putExtra(TransactionIntentExtra.transactionRecord, data.record)
                })
            }
        }

        binding.tvDate.text = "${currentCalendar.get(Calendar.MONTH) + 1}?????????"
        binding.tvDate.setOnClickListener {
            timePickerView.setDate(currentCalendar)
            timePickerView.show(true)
        }

        injectObserver()

    }

    override fun onResume() {
        super.onResume()
        queryRecords()
    }

    private fun injectObserver() {

        launchAndRepeatWithViewLifecycle {
            vm.transactionRecords.collect {
                binding.rvTransactionRecord.models = it
            }
        }

        launchAndRepeatWithViewLifecycle {
            vm.transactionRecordAmountSum.collect {
                binding.tvPayAmount.text = it
            }
        }
    }

    private fun queryRecords() {
        //??????????????????, set(Calendar.HOUR_OF_DAY, 0) ????????? 12:00:00??? ???????????? JDK?????? ?
        //currentCalendar.timeInMillis ????????? 8:00:00
        //currentCalendar.time.time ????????? 12:00:00
        // ?????????google???????????????
        currentCalendar.set(Calendar.HOUR_OF_DAY, 0)
        currentCalendar.set(Calendar.MINUTE, 0)
        currentCalendar.set(Calendar.SECOND, 0)
        currentCalendar.set(Calendar.MILLISECOND, 0)


        val first = currentCalendar.getActualMinimum(Calendar.DAY_OF_MONTH)
        currentCalendar.set(Calendar.DAY_OF_MONTH, first)
        val start = currentCalendar.time.time

        val second: Int = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        currentCalendar.set(Calendar.DAY_OF_MONTH, second)
        currentCalendar.set(Calendar.HOUR_OF_DAY, 23)
        currentCalendar.set(Calendar.MINUTE, 59)
        currentCalendar.set(Calendar.SECOND, 59)
        val end = currentCalendar.time.time

        vm.getRecordTime(start, end)
        Logger.d("start: $start, end: $end")
    }
}