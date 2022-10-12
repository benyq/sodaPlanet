package com.benyq.sodaplanet.transaction.ui.add

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.benyq.sodaplanet.base.base.BaseFragment
import com.benyq.sodaplanet.base.ext.fullScreen
import com.benyq.sodaplanet.base.ext.load
import com.benyq.sodaplanet.base.ext.setStatusBarMode
import com.benyq.sodaplanet.transaction.R
import com.benyq.sodaplanet.transaction.data.ConsumeType
import com.benyq.sodaplanet.transaction.data.PaidType
import com.benyq.sodaplanet.transaction.databinding.FragmentTransactionAddRecordBinding
import com.benyq.sodaplanet.transaction.widget.PayPanelView
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch

/**
 *
 * @author benyq
 * @date 2022/10/10
 * @email 1520063035@qq.com
 *
 */
class TransactionAddRecordFragment : BaseFragment<FragmentTransactionAddRecordBinding>() {

    private val vm by viewModels<TransactionAddRecordViewModel>()

    private var currentConsumeType = ConsumeType.singleFood

    private lateinit var paidTypeDialog: BottomPaidTypeDialog

    override fun provideViewBinding() = FragmentTransactionAddRecordBinding.inflate(layoutInflater)

    override fun onFragmentViewCreated(view: View) {
        binding.rvConsume.grid(4).setup {
            var oldPosition = 0
            addType<ConsumeType>(R.layout.item_consume_type)
            onBind {
                val data = getModel<ConsumeType>()
                findView<ImageView>(R.id.ivConsume).load(data.resId)
                findView<ImageView>(R.id.ivBG).setBackgroundColor(
                    ContextCompat.getColor(
                        requireActivity(),
                        data.colorId
                    )
                )
                findView<ImageView>(R.id.ivCover).isVisible = data.selected
                findView<TextView>(R.id.tvContent).text = data.message
            }
            R.id.item.onClick {
                getModel<ConsumeType>(oldPosition).selected = false
                getModel<ConsumeType>(layoutPosition).selected = true
                notifyItemChanged(oldPosition)
                notifyItemChanged(layoutPosition)
                oldPosition = layoutPosition

                currentConsumeType = getModel()
            }
        }.models = ConsumeType.consumeTypes()

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.payPanelView.itemListener = object : PayPanelView.PayPanelClickListener {
            override fun onClickDate() {
            }

            override fun onClickDone(amount: String, paidType: PaidType) {
                vm.addTransactionRecord(amount, currentConsumeType, paidType)
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
                        Toast.makeText(requireContext(), "数据添加失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}