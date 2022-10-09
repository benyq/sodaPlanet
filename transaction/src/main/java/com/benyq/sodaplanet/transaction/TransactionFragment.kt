package com.benyq.sodaplanet.transaction

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.benyq.sodaplanet.base.base.BaseFragment
import com.benyq.sodaplanet.transaction.databinding.FragmentTransactionBinding

/**
 *
 * @author benyq
 * @date 2022/10/9
 * @email 1520063035@qq.com
 *
 */
class TransactionFragment : BaseFragment<FragmentTransactionBinding>(){

    override fun provideViewBinding() = FragmentTransactionBinding.inflate(layoutInflater)

    override fun onFragmentViewCreated(view: View) {

    }

}