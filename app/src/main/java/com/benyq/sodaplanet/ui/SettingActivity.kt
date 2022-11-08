package com.benyq.sodaplanet.ui

import android.content.Intent
import android.os.Bundle
import com.benyq.sodaplanet.base.ui.BaseActivity
import com.benyq.sodaplanet.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    override fun provideViewBinding() = ActivitySettingBinding.inflate(layoutInflater)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        binding.headerView.setBackAction {
            finish()
        }
        binding.ifAboutApp.setOnClickListener {
            startActivity(Intent(this, AboutAppActivity::class.java))
        }
    }
}