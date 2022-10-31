package com.benyq.sodaplanet.base.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.benyq.sodaplanet.base.room.dao.MediaFileEntityDao
import com.benyq.sodaplanet.base.room.dao.TransactionRecordDao
import com.benyq.sodaplanet.base.room.entity.MediaFileEntity
import com.benyq.sodaplanet.base.room.entity.TransactionRecord

/**
 *
 * @author benyq
 * @date 2022/9/30
 * @email 1520063035@qq.com
 * 数据库
 */
@Database(entities = [TransactionRecord::class, MediaFileEntity::class], version = 1, exportSchema = false)
abstract class SodaPlanetRoomDB : RoomDatabase(){

    abstract fun transactionRecordDao(): TransactionRecordDao
    abstract fun mediaFileEntityDao(): MediaFileEntityDao

}


lateinit var sodaPlanetDB: SodaPlanetRoomDB