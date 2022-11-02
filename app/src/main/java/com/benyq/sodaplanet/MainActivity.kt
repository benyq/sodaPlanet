package com.benyq.sodaplanet

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.activity.viewModels
import com.benyq.sodaplanet.audio.wifi.WifiTransferActivity
import com.benyq.sodaplanet.base.ext.BitmapUtils
import com.benyq.sodaplanet.base.ext.windowSize
import com.benyq.sodaplanet.base.ui.BaseActivity
import com.benyq.sodaplanet.databinding.ActivityMainBinding
import java.io.File

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val vm by viewModels<MainViewModel>()

    override fun provideViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        lifecycle.addObserver(ClipboardHandleObserver())

        binding.tvCheckVersion.setOnClickListener {
            startActivity(Intent(this, WifiTransferActivity::class.java))
        }
    }


    private fun upBackgroundImage() {
        val path = getExternalFilesDir("bg")?.absolutePath + File.separator + "宁静夜色.jpg"
        val bgImage = BitmapUtils.decodeBitmap(
            path,
            windowManager.windowSize.widthPixels,
            windowManager.windowSize.heightPixels
        )
        window.decorView.background = BitmapDrawable(resources, bgImage)
    }
}