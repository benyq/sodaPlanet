package com.benyq.sodaplanet.base.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.benyq.sodaplanet.base.room.entity.TransactionRecord

/**
 *
 * @author benyq
 * @date 2022/9/30
 * @email 1520063035@qq.com
 *
 */
@Dao
interface TransactionRecordDao {
    @Query("SELECT * FROM transaction_record")
    fun getAll(): List<TransactionRecord>

    @Insert
    fun addTransactionRecord(vararg record: TransactionRecord)
}