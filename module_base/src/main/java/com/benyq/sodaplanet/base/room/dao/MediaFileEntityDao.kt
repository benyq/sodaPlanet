package com.benyq.sodaplanet.base.room.dao

import androidx.room.Dao
import androidx.room.Insert
import com.benyq.sodaplanet.base.room.entity.MediaFileEntity

/**
 *
 * @author benyq
 * @date 2022/10/31
 * @email 1520063035@qq.com
 *
 */
@Dao
interface MediaFileEntityDao {


    @Insert
    fun addMediaFile(entity: MediaFileEntity): Long



}