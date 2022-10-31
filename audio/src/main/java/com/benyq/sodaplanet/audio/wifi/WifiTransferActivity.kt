package com.benyq.sodaplanet.audio.wifi

import android.annotation.SuppressLint
import android.os.Bundle
import com.benyq.sodaplanet.audio.databinding.ActivityWifiTransferBinding
import com.benyq.sodaplanet.base.ui.BaseActivity
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

@SuppressLint("SetTextI18n")
class WifiTransferActivity : BaseActivity<ActivityWifiTransferBinding>() {

    private val server: SimpleFileServer by lazy { SimpleFileServer(Defaults.port) }

    override fun provideViewBinding() = ActivityWifiTransferBinding.inflate(layoutInflater)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        binding.headerView.setBackAction { finish() }

        binding.tvWifiIp.text = "http://${getConnectWifiIp()}:${Defaults.port}"
        binding.tvWifiName.text = getWifiSSD()

        server.start()

        XXPermissions.with(this).permission(Permission.ACCESS_FINE_LOCATION)
            .request { permissions, all ->
                if (all) {
                    binding.tvWifiName.text = getWifiSSD()
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        server.stop()
    }
}

