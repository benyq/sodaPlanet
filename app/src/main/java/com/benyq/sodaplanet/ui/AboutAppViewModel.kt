package com.benyq.sodaplanet.ui

import android.app.Application
import com.benyq.sodaplanet.net.entity.ApkVersionEntity
import com.benyq.sodaplanet.base.ui.BaseViewModel
import com.benyq.sodaplanet.sodaApi
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 *
 * @author benyq
 * @date 2022/10/26
 * @email 1520063035@qq.com
 *
 */
class AboutAppViewModel(application: Application): BaseViewModel(application) {


    private val _apkVersion: MutableSharedFlow<ApkVersionEntity> = MutableSharedFlow()
    val apkVersion: SharedFlow<ApkVersionEntity> = _apkVersion

    fun checkApkVersion() {
        execute {
            sodaApi.apkVersion()
        }.onError {
            it.printStackTrace()
            Logger.e("checkApkVersion: error: ${it.message}")
        }.onSuccess {
            _apkVersion.emit(it)
        }
    }

}