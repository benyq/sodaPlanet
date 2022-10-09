package com.benyq.sodaplanet.base.ext

/**
 *
 * @author benyq
 * @date 2022/10/9
 * @email 1520063035@qq.com
 *
 */

fun <T> String.toNumberDefault(default: T) : T {
    return try {
        val res: Any = when (default) {
            is Long -> toLong()
            is Int -> toInt()
            is Float -> toFloat()
            is Double -> toDouble()
            else ->  throw NumberFormatException("未找到该类型")
        }
        res as T
    }catch (e: NumberFormatException) {
        default
    }
}