package com.benyq.sodaplanet.transaction.ui.analysis

import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.benyq.sodaplanet.base.ui.BaseFragment
import com.benyq.sodaplanet.base.ext.launchAndRepeatWithViewLifecycle
import com.benyq.sodaplanet.transaction.R
import com.benyq.sodaplanet.transaction.data.TransactionConsumeData
import com.benyq.sodaplanet.transaction.databinding.FragmentTransactionAyalysisBinding
import com.benyq.sodaplanet.transaction.widget.LineChartView
import com.benyq.sodaplanet.transaction.widget.PieChartView
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.orhanobut.logger.Logger
import kotlin.properties.Delegates

/**
 *
 * @author benyq
 * @date 2022/10/10
 * @email 1520063035@qq.com
 *
 */
class TransactionAnalysisFragment : BaseFragment<FragmentTransactionAyalysisBinding>() {

    private val vm by viewModels<TransactionAnalysisViewModel>()

    override fun provideViewBinding() = FragmentTransactionAyalysisBinding.inflate(layoutInflater)

    private var currentDateType by Delegates.observable(LineChartView.DateType.WEEK) { property, old, new  ->
        binding.tvAverage.text = when (new) {
            LineChartView.DateType.WEEK -> "日均:"
            LineChartView.DateType.MONTH -> "日均:"
            LineChartView.DateType.YEAR -> "月均:"
        }
    }

    override fun onFragmentViewCreated(view: View) {

        binding.rvConsumeOrder.linear().setup {
            addType<TransactionConsumeData>(R.layout.item_transaction_order)
        }

        binding.rgDate.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rbWeek -> {
                    currentDateType = LineChartView.DateType.WEEK
                }
                R.id.rbMonth -> {
                    currentDateType = LineChartView.DateType.MONTH
                }
                R.id.rbYear -> {
                    currentDateType = LineChartView.DateType.YEAR
                }
            }
            getData()
        }

        binding.rbWeek.isChecked = true

        injectObserver()

    }

    override fun onResume() {
        super.onResume()
        getData()

    }

    private fun getData() {
        when (currentDateType) {
            LineChartView.DateType.WEEK -> vm.getWeekData()
            LineChartView.DateType.MONTH -> vm.getMonthData()
            LineChartView.DateType.YEAR -> vm.getYearData()
        }

    }

    private fun injectObserver() {
        launchAndRepeatWithViewLifecycle {
            vm.dateTransactionData.collect {
                Logger.d("dateTransactionData: $it")
                binding.lineChart.setData(it.first.map { data ->
                    LineChartView.Entity(data.date, data.amount)
                }, currentDateType)
                binding.tvTotalAmount.text = "%.2f".format(it.second)
                binding.tvTotalAverage.text = "%.2f".format(it.second / it.first.size)
            }
        }
        launchAndRepeatWithViewLifecycle {
            vm.consumeTransactionData.collect {
                binding.rvConsumeOrder.models = it
                binding.pieChart.setData(it.map { data->
                    PieChartView.PieData(data.consumeType.message, data.record.amount, data.ratio, ContextCompat.getColor(requireContext(),data.consumeType.colorId))
                })
            }
        }
    }
}