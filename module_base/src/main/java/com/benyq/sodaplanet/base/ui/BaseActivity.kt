package com.benyq.sodaplanet.base.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.benyq.sodaplanet.base.R
import com.benyq.sodaplanet.base.ext.getColorRef
import com.benyq.sodaplanet.base.ext.setStatusBarMode

/**
 *
 * @author benyq
 * @date 2022/10/14
 * @email 1520063035@qq.com
 *
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(){

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarMode(getColorRef(R.color.dark_grey), true)

        _binding = provideViewBinding()
        setContentView(binding.root)
        onActivityCreated(savedInstanceState)
    }


    abstract fun onActivityCreated(savedInstanceState: Bundle?)
    abstract fun provideViewBinding(): VB

}