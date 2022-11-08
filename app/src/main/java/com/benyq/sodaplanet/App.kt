package com.benyq.sodaplanet

import android.app.Application
import com.benyq.sodaplanet.base.CommonModuleInit
import com.benyq.sodaplanet.base.net.RetrofitFactory
import com.benyq.sodaplanet.net.SodaPlanetApi

/**
 *
 * @author benyq
 * @date 2022/9/30
 * @email 1520063035@qq.com
 *
 */

lateinit var sodaApi: SodaPlanetApi

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        CommonModuleInit.onInit(this)

        val baseUrl = assets.open("net").bufferedReader().readText()
        sodaApi = RetrofitFactory.create(SodaPlanetApi::class.java, baseUrl, okhttpConfig = {})
    }
}