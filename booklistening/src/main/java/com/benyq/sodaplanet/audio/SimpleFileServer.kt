package com.benyq.sodaplanet.audio

import android.text.TextUtils
import com.benyq.sodaplanet.base.ext.appCtx
import com.benyq.sodaplanet.base.ext.toast
import com.orhanobut.logger.Logger
import fi.iki.elonen.NanoHTTPD
import java.io.UnsupportedEncodingException

/**
 *
 * @author benyq
 * @date 2022/10/28
 * @email 1520063035@qq.com
 *
 */
class SimpleFileServer(port: Int) : NanoHTTPD(port) {

    private var lastFileName = ""

    override fun serve(session: IHTTPSession?): Response {

        session?.let {

            if (Method.GET == session.method) {
                if (it.uri.contains("index.html") || it.uri == "/") {
                    val fis = appCtx.assets.open("uploader/index.html")
                    return newFixedLengthResponse(Response.Status.OK,
                        "text/html", fis, fis.available().toLong())
                }else {
                    // 获取文件类型
                    val uri = it.uri
                    // 获取文件类型
                    val type: String? = Defaults.extensions[uri.substring(uri.lastIndexOf(".") + 1)]
                    if (TextUtils.isEmpty(type)) return newFixedLengthResponse("")
                    // 读取文件
                    // 读取文件
                    val fis = appCtx.assets.open("uploader$uri")
                    return if (fis.available() < 1) newFixedLengthResponse("") else newFixedLengthResponse(
                        Response.Status.OK,
                        type,
                        fis, fis.available().toLong()
                    )
                }
            }else if (Method.POST == session.method) {
                val files = mutableMapOf<String, String>()
                try {
                    it.parseBody(files)
                }catch (e: Exception) {
                    e.printStackTrace()
                }
                if (it.uri.contains("send_file_name")) {
                    lastFileName = it.parameters[""]?.get(0) ?: ""
                } else if (it.uri.contains("files")) {
                    files.forEach { map ->
                        //拷贝文件
                    }
                }
            }
        }

        return newFixedLengthResponse("404")
    }

}