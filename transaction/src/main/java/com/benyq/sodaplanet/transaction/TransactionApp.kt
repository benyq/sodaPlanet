package com.benyq.sodaplanet.transaction

import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.benyq.sodaplanet.base.CommonModuleInit
import com.benyq.sodaplanet.base.IModuleInit
import com.benyq.sodaplanet.base.ext.fromM
import com.benyq.sodaplanet.base.ext.fromN_MR1
import com.benyq.sodaplanet.transaction.data.TransactionIntentExtra
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

        if (fromN_MR1()) {
            val shortScan = ShortcutInfoCompat.Builder(app, "transaction")//唯一标识id
                .setShortLabel("记账")//短标签
                .setIcon(IconCompat.createWithResource(app, R.drawable.ic_transaction))//图标
                //跳转的目标，定义Activity
                .setIntent(Intent(Intent.ACTION_MAIN, null, app, TransactionActivity::class.java).apply {
                    putExtra(TransactionIntentExtra.shortcut, true)
                })
                .build()
            //执行添加操作
            ShortcutManagerCompat.addDynamicShortcuts(app, mutableListOf(shortScan))
        }
    }
}