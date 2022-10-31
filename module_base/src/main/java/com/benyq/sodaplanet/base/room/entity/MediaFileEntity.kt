package com.benyq.sodaplanet.base.room.entity

import android.content.ContentResolver.MimeTypeInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 * @author benyq
 * @date 2022/10/31
 * @email 1520063035@qq.com
 *
 */
@Entity(tableName = "t_media_file")
data class MediaFileEntity(
    var name: String,
    var path: String,//全路径
    val mimeType: String,
    var createTime: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
)