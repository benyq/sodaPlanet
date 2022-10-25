package com.benyq.sodaplanet.transaction.ui

import android.graphics.Color
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.benyq.sodaplanet.base.ui.BaseFragment
import com.benyq.sodaplanet.base.ext.setStatusBarMode
import com.benyq.sodaplanet.transaction.R
import com.benyq.sodaplanet.transaction.databinding.FragmentTransactionBinding
import com.benyq.sodaplanet.transaction.ui.analysis.TransactionAnalysisFragment
import com.benyq.sodaplanet.transaction.ui.record.TransactionRecordFragment

/**
 *
 * @author benyq
 * @date 2022/10/9
 * @email 1520063035@qq.com
 *
 */
class TransactionFragment : BaseFragment<FragmentTransactionBinding>() {

    private val vm by viewModels<TransactionViewModel>()

    override fun provideViewBinding() = FragmentTransactionBinding.inflate(layoutInflater)

    override fun onFragmentViewCreated(view: View) {

        setStatusBarMode(Color.WHITE, true)

        binding.vpFragment.isUserInputEnabled = false
        binding.vpFragment.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 2
            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> TransactionRecordFragment()
                    else -> TransactionAnalysisFragment()
                }
            }
        }
        binding.vpFragment.currentItem = vm.currentIndex
        binding.tvRecord.isSelected = vm.currentIndex == 0
        binding.tvAnalysis.isSelected = vm.currentIndex != 0

        val clickListener = View.OnClickListener {
            val isRecord = it.id == R.id.tvRecord
            binding.tvRecord.isSelected = isRecord
            binding.tvAnalysis.isSelected = !isRecord
            vm.currentIndex = if (isRecord) 0 else 1
            binding.vpFragment.currentItem = vm.currentIndex
        }

        binding.tvRecord.setOnClickListener(clickListener)
        binding.tvAnalysis.setOnClickListener(clickListener)
        binding.ivAddRecord.setOnClickListener {
            findNavController().navigate(
                R.id.action_home_to_add, null, NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_bottom)
                    .setExitAnim(R.anim.anim_normal)
                    .setPopExitAnim(R.anim.slide_out_bottom)
                    .build()
            )
        }
    }


}