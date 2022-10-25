package com.benyq.sodaplanet

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 *
 * @author benyq
 * @date 2022/10/25
 * @email 1520063035@qq.com
 * 下载最新版apk
 */
class ApkDownloadService : Service(){
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

    }
}