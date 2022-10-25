package com.benyq.sodaplanet

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author benyq
 * @email 1520063035@qq.com
 * @date 2022/7/17
 */
@Parcelize
data class AppEntity(val version: Int, val appBundle: List<AppBundle>): Parcelable {
    @Parcelize
    data class AppBundle(val bundleName: String, val patterns: List<String>): Parcelable

    companion object {
        val default = AppEntity(
            1,
            listOf(
                AppBundle("com.sankuai.meituan", listOf(".*小美果园.*")),
                AppBundle("com.xunmeng.pinduoduo", listOf("[0-9]{5,}\\s+.*")),
                AppBundle("com.jingdong.app.mall", listOf("[0-9]{2}:/.*")),
                AppBundle("com.kuaishou.nebula", listOf("₤X.*", "ӨX.*")),
            )
        )
    }
}
