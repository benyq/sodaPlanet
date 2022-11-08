package com.benyq.sodaplanet.net

import com.benyq.sodaplanet.net.entity.ApkVersionEntity
import com.benyq.sodaplanet.net.entity.AppEntity
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

