package com.benyq.sodaplanet.base.net

/**
 *
 * @author benyq
 * @date 2022/4/1
 * @email 1520063035@qq.com
 *
 */
sealed class NetResultData<out T> {
    object Start : NetResultData<Nothing>()
    data class Success<T>(val data: T) : NetResultData<T>()
    data class Error(val t: Throwable) : NetResultData<Nothing>()
    object Finally : NetResultData<Nothing>()
}
