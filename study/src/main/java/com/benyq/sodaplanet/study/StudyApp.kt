package com.benyq.sodaplanet.study

import android.app.Application
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.benyq.sodaplanet.base.CommonModuleInit
import com.benyq.sodaplanet.base.IModuleInit
import com.benyq.sodaplanet.base.ext.fromN_MR1
import com.drake.brv.utils.BRV

/**
 *
 * @author benyq
 * @date 2022/11/8
 * @email 1520063035@qq.com
 *
 */
class StudyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        CommonModuleInit.onInit(this)
    }
}

class StudyInit: IModuleInit {
    override fun onInitAhead(app: Application) {

    }
}