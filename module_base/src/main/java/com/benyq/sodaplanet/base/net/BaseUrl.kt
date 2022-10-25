package com.benyq.sodaplanet.base.net

/**
 *
 * @author benyq
 * @date 2022/2/9
 * @email 1520063035@qq.com
 *
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class BaseUrl(val value: String) {
}