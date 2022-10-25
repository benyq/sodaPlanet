package com.benyq.sodaplanet.transaction.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.benyq.sodaplanet.base.ui.BaseFragment
import com.benyq.sodaplanet.base.ext.launchAndRepeatWithViewLifecycle
import com.benyq.sodaplanet.base.ext.toast
import com.benyq.sodaplanet.base.room.entity.TransactionRecord
import com.benyq.sodaplanet.transaction.R
import com.benyq.sodaplanet.transaction.data.TransactionIntentExtra
import com.benyq.sodaplanet.transaction.data.TransactionRecordData
import com.benyq.sodaplanet.transaction.databinding.FragmentTransactionRecordDetailBinding

/**
 *
 * @author benyq
 * @date 2022/10/14
 * @email 1520063035@qq.com
 *
 */
class TransactionRecordDetailFragment : BaseFragment<FragmentTransactionRecordDetailBinding>() {

    private val vm by viewModels<TransactionRecordDetailViewModel>()

    private lateinit var recordData: TransactionRecordData

    override fun provideViewBinding() =
        FragmentTransactionRecordDetailBinding.inflate(layoutInflater)

    override fun onFragmentViewCreated(view: View) {

        binding.ivBack.setOnClickListener {
            requireActivity().finish()
        }

        val record =
            arguments?.getParcelable<TransactionRecord>(TransactionIntentExtra.transactionRecord)
                ?: return
        recordData = TransactionRecordData(record)
        binding.m = recordData

        binding.tvDelete.setOnClickListener {
            vm.deleteRecord(record)
        }

        binding.tvEdit.setOnClickListener {
            findNavController().navigate(
                R.id.action_home_to_add, Bundle().apply {
                    putParcelable(TransactionIntentExtra.transactionRecord, record)
                }, NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_bottom)
                    .setExitAnim(R.anim.anim_normal)
                    .setPopExitAnim(R.anim.slide_out_bottom)
                    .build()
            )
        }
        injectObserver()
    }

    private fun injectObserver() {
        launchAndRepeatWithViewLifecycle {
            vm.deleteRecordResult.collect {
                if (it) {
                    toast("记录删除成功")
                    requireActivity().finish()
                }else {
                    toast("记录删除失败")
                }
            }
        }
    }
}