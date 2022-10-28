package com.benyq.sodaplanet.audio

import android.annotation.SuppressLint
import android.os.Bundle
import com.benyq.sodaplanet.audio.databinding.ActivityWifiTransferBinding
import com.benyq.sodaplanet.base.ui.BaseActivity

@SuppressLint("SetTextI18n")
class WifiTransferActivity : BaseActivity<ActivityWifiTransferBinding>() {

    override fun provideViewBinding() = ActivityWifiTransferBinding.inflate(layoutInflater)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        binding.tvWifiIp.text = "http://${getConnectWifiIp()}:10086"
        binding.tvWifiName.text = getWifiSSD()

        val server = SimpleFileServer(10086)
        server.start()
    }
}

