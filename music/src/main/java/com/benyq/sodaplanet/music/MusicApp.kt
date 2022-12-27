package com.benyq.sodaplanet.music

import android.app.Application
import com.benyq.sodaplanet.base.CommonModuleInit
import com.benyq.sodaplanet.base.IModuleInit

/**
 *
 * @author benyq
 * @date 2022/11/8
 * @email 1520063035@qq.com
 *
 */
class MusicApp : Application(){
    override fun onCreate() {
        super.onCreate()
        CommonModuleInit.onInit(this)
    }
}

class MusicInit: IModuleInit {
    override fun onInitAhead(app: Application) {
        MusicNotificationHelper.init(app)
    }
}