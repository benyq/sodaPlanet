package com.benyq.sodaplanet.transaction

import android.app.Application
import com.benyq.sodaplanet.base.CommonModuleInit
import com.benyq.sodaplanet.base.IModuleInit
import com.drake.brv.utils.BRV

/**
 *
 * @author benyq
 * @date 2022/9/29
 * @email 1520063035@qq.com
 *
 */
class TransactionApp : Application(){
    override fun onCreate() {
        super.onCreate()
        CommonModuleInit.onInit(this)
    }
}


class TransactionInit: IModuleInit {
    override fun onInitAhead(app: Application) {
        BRV.modelId = BR.m
    }
}