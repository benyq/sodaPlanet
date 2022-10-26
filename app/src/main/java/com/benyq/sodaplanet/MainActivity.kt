package com.benyq.sodaplanet

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.benyq.sodaplanet.base.ui.BaseActivity
import com.benyq.sodaplanet.databinding.ActivityMainBinding
import com.benyq.sodaplanet.transaction.TransactionActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val vm by viewModels<MainViewModel>()

    override fun provideViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        lifecycle.addObserver(ClipboardHandleObserver())

        binding.tvCheckVersion.setOnClickListener {
            startActivity(Intent(this, AboutAppActivity::class.java))
        }
    }

}