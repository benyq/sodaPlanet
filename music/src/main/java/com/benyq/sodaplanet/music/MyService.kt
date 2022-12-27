package com.benyq.sodaplanet.music

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.orhanobut.logger.Logger

class MyService : Service() {

    override fun onBind(intent: Intent) = null

    override fun onCreate() {
        super.onCreate()
        Logger.d("MyService onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Logger.d("MyService onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }
}