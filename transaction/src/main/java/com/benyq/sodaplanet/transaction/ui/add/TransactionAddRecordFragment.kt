package com.benyq.sodaplanet.transaction.ui.add

import android.view.View
import android.widget.Toast
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
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import kotlinx.coroutines.launch

/**
 *
 * @author benyq
 * @date 2022/10/10
 * @email 1520063035@qq.com
 * add and edit
 */
class TransactionAddRecordFragment : BaseFragment<FragmentTransactionAddRecordBinding>() {

    private val vm by viewModels<TransactionAddRecordViewModel>()

    private var currentConsumeType = ConsumeType.singleFood

    private lateinit var paidTypeDialog: BottomPaidTypeDialog

    private var record: TransactionRecord? = null

    override fun provideViewBinding() = FragmentTransactionAddRecordBinding.inflate(layoutInflater)

    override fun onFragmentViewCreated(view: View) {
        val consumeTypes = ConsumeType.consumeTypes()
        record = arguments?.getParcelable(TransactionIntentExtra.transactionRecord)
        record?.let {
            binding.payPanelView.setDefaultData(it.amount, it.paidType, it.note)
            consumeTypes.forEach { consume->
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
            }

            override fun onClickDone(amount: String, paidType: PaidType, note: String) {
                if (record == null) {
                    vm.addTransactionRecord(amount, currentConsumeType, paidType, note)
                }else {
                    record?.let {
                        it.paidType = paidType.code
                        it.consumeType = currentConsumeType.code
                        it.note = note
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