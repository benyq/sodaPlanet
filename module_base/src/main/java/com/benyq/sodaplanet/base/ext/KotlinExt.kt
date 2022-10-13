package com.benyq.sodaplanet.base.ext

import android.util.Log
/**
 *
 * @author benyq
 * @date 2022/10/11
 * @email 1520063035@qq.com
 *
 */


fun Boolean?.isTrue(
    error: (String) -> Unit = { Log.e("DoKit", it) },
    isFalse: () -> Unit = {},
    action: () -> Unit
) {
    if (this == null) {
        error("Boolean is null")
    }
    if (this == true) {
        action()
    } else {
        isFalse()
    }
}


/**
 * Boolean 扩展函数
 */
fun Boolean?.isFalse(
    error: (String) -> Unit = { Log.e("DoKit", it) },
    isTrue: () -> Unit = {},
    action: () -> Unit
) {
    if (this == null) {
        error("Boolean is null")
    }
    if (this == false) {
        action()
    } else {
        isTrue()
    }
}

