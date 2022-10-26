package com.benyq.sodaplanet.base.net

import com.orhanobut.logger.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */
object RetrofitFactory {

    // 读超时
    const val READ_TIME_OUT = 20L
    // 写超时
    const val WRITE_TIME_OUT = 30L
    // 连接超时
    const val CONNECT_TIME_OUT = 5L

    fun <T> create(clz: Class<T>, baseUrl: String, okhttpConfig: (OkHttpClient.Builder.()->Unit) ? = null, retrofitConfig: (Retrofit.Builder.()->Unit)? = null): T {

        val loggingInterceptor = HttpLoggingInterceptor { message ->
            Logger.d(message)
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
        }
        okhttpConfig?.invoke(builder)

        val retrofit = createRetrofit(baseUrl, builder, retrofitConfig)

        return retrofit.create(clz)
    }

    private fun createRetrofit(baseUrl: String, builder: OkHttpClient.Builder, retrofitAction: (Retrofit.Builder.()->Unit)?): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .apply {
                retrofitAction?.invoke(this) ?: addConverterFactory(GsonConverterFactory.create())
            }
            .client(builder.build())
            .build()
    }
}