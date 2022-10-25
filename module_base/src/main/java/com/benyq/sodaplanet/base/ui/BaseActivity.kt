package com.benyq.sodaplanet.base.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

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
        _binding = provideViewBinding()
        setContentView(binding.root)
        onActivityCreated(savedInstanceState)
    }


    abstract fun onActivityCreated(savedInstanceState: Bundle?)
    abstract fun provideViewBinding(): VB

}