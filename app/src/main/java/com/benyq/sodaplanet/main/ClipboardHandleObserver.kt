package com.benyq.sodaplanet.main

import android.content.Context
import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.benyq.sodaplanet.base.coroutine.Coroutine
import com.benyq.sodaplanet.base.ext.appCtx
import com.benyq.sodaplanet.base.ext.clipboardRead
import com.benyq.sodaplanet.base.ext.runOnUIThread
import com.benyq.sodaplanet.base.ext.toast
import com.benyq.sodaplanet.net.entity.AppEntity
import com.benyq.sodaplanet.sodaApi
import com.drake.serialize.serialize.serial
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 *
 * @author benyq
 * @date 2022/10/25
 * @email 1520063035@qq.com
 *
 */
class ClipboardHandleObserver : DefaultLifecycleObserver{

    private var appJson: AppEntity by serial(AppEntity.default)
    private var oldClipboardContent: String = ""

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        runOnUIThread(50) {
            loadAppJson(appCtx, owner.lifecycleScope)
        }
    }


    private fun loadAppJson(context: Context, scope: CoroutineScope) {
        val content = context.clipboardRead()
        if (content == oldClipboardContent) return
        oldClipboardContent = content

        Logger.i("content", content)

        if (content.isEmpty()) {
            return
        }

        Coroutine.async(scope, Dispatchers.IO) {
            sodaApi.appJson()
        }.onSuccess {
            if (it.version > appJson.version) {
                appJson = it
            }
            handle(context, appJson, content)
        }.onError {
            it.printStackTrace()
            Logger.e("appJson error: ${it.message}")
        }

    }

    private fun handle(context: Context, appJson: AppEntity, content: String) {
        var intent: Intent? = null
        appJson.appBundle.forEach intentFor@{ bundle ->
            bundle.patterns.forEach { pattern ->
                if (content.matches(Regex(pattern))) {
                    intent = context.packageManager.getLaunchIntentForPackage(bundle.bundleName)
                    return@intentFor
                }
            }
        }
        intent?.let {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            appCtx.startActivity(it)
        } ?: appCtx.toast("找不到对应APP, $content")
    }
}