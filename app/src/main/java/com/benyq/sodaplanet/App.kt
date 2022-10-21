package com.benyq.sodaplanet

import android.app.Application
import com.benyq.sodaplanet.base.CommonModuleInit

/**
 *
 * @author benyq
 * @date 2022/9/30
 * @email 1520063035@qq.com
 *
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        CommonModuleInit.onInit(this)
    }
}