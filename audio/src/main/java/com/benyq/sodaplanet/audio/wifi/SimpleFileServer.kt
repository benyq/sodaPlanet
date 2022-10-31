package com.benyq.sodaplanet.audio.wifi

import android.media.MediaMetadataRetriever
import android.text.TextUtils
import com.benyq.sodaplanet.base.ext.appCtx
import com.benyq.sodaplanet.base.room.entity.MediaFileEntity
import com.benyq.sodaplanet.base.room.sodaPlanetDB
import com.orhanobut.logger.Logger
import fi.iki.elonen.NanoHTTPD
import java.io.File

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

            Logger.d("Method: ${session.method}, uri: ${it.uri}")

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
                    lastFileName = it.parameters["filename"]?.get(0) ?: ""
                } else if (it.uri.contains("delete_file")) {
                    Logger.d("Method: ${session.method}, parameters: ${it.parameters}")
                    val deleteFilePath = it.parameters["file_name"]?.get(0) ?: ""
                    if (deleteFilePath.isNotEmpty()) File(appCtx.getExternalFilesDir("media")?.absolutePath + File.separator + deleteFilePath).delete()
                }else if (it.uri.contains("files")) {
                    files.forEach { map ->
                        //拷贝文件
                        val tempName = map.value.substringAfterLast("/")
                        if (lastFileName.isEmpty()) lastFileName = tempName
                        val destPath = appCtx.getExternalFilesDir("media")?.absolutePath + File.separator + lastFileName
                        val mimeType = getMimeType(map.value)

                        val tempFile = File(map.value)
                        val destFile = File(destPath)
                        tempFile.copyTo(destFile, true)

                        val mediaEntity = MediaFileEntity(lastFileName, destPath, mimeType)
                        addMediaFileToDB(mediaEntity)
                        return@forEach
                    }
                }
            }
        }

        return newFixedLengthResponse("404")
    }


    private fun getMimeType(path: String): String {
        var mime = ""
        val mmr = MediaMetadataRetriever()
        try {
            mmr.setDataSource(path)
            mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE) ?: "text/plain"
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return mime
    }

    private fun addMediaFileToDB(mediaEntity: MediaFileEntity) {
        val id = sodaPlanetDB.mediaFileEntityDao().addMediaFile(mediaEntity)

        Logger.d("mediaEntity: $mediaEntity, id: $id")

    }
}