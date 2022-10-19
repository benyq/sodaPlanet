package com.benyq.sodaplanet.transaction.ui.analysis

import android.view.View
import com.benyq.sodaplanet.base.base.BaseFragment
import com.benyq.sodaplanet.base.ext.runOnUIThread
import com.benyq.sodaplanet.transaction.databinding.FragmentTransactionAyalysisBinding
import com.benyq.sodaplanet.transaction.widget.LineChartView

/**
 *
 * @author benyq
 * @date 2022/10/10
 * @email 1520063035@qq.com
 *
 */
class TransactionAnalysisFragment : BaseFragment<FragmentTransactionAyalysisBinding>(){

    override fun provideViewBinding() = FragmentTransactionAyalysisBinding.inflate(layoutInflater)

    override fun onFragmentViewCreated(view: View) {
        runOnUIThread(50) {
            binding.lineChart.setData(listOf(LineChartView.Entity("1月", 1034f),
                LineChartView.Entity("2月", 10f),
                LineChartView.Entity("3月", 23f),
                LineChartView.Entity("4月", 456f),
                LineChartView.Entity("5月", 456f),
                LineChartView.Entity("6月", 23f),
                LineChartView.Entity("7月", 23f),
                LineChartView.Entity("8月", 1f),
                LineChartView.Entity("9月", 1f),
                LineChartView.Entity("10月", 1f),
                LineChartView.Entity("11月", 1f),
                LineChartView.Entity("12月", 1f),
            ), LineChartView.DateType.WEEK)
        }
    }
}