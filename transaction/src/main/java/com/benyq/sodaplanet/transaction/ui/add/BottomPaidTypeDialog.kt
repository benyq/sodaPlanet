package com.benyq.sodaplanet.transaction.ui.add

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.benyq.sodaplanet.transaction.R
import com.benyq.sodaplanet.transaction.data.PaidType
import com.benyq.sodaplanet.transaction.databinding.DialogBottomPaidTypeBinding
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 *
 * @author benyq
 * @date 2022/10/11
 * @email 1520063035@qq.com
 *
 */
class BottomPaidTypeDialog(context: Context, val onItemClick: (item: PaidType) -> Unit) : BottomSheetDialog(context){
    private lateinit var binding: DialogBottomPaidTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogBottomPaidTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPaidType.linear().setup {
            addType<PaidType>(R.layout.item_paid_type)
            onBind {
                val data = getModel<PaidType>()
                findView<ImageView>(R.id.ivPaidType).setImageResource(data.resId)
                findView<TextView>(R.id.tvPaidType).text = data.message
            }
            R.id.item.onClick {
                val data = getModel<PaidType>()
                onItemClick(data)
                dismiss()
            }
        }.models = PaidType.paidTypes
    }


}