package com.benyq.sodaplanet

import com.benyq.sodaplanet.base.net.RetrofitFactory
import retrofit2.http.GET

/**
 * @author benyq
 * @email 1520063035@qq.com
 * @date 2022/7/17
 */

interface SodaPlanetApi {

    @GET("/appBundle.json")
    suspend fun appJson(): AppEntity

    @GET("/appVersion.json")
    suspend fun apkVersion(): ApkVersionEntity
}

