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
    @Query("select * from transaction_record")
    fun getAll(): List<TransactionRecord>

    @Insert
    fun addTransactionRecord(vararg record: TransactionRecord)


    @Query("select * from transaction_record where (:consumeType is not null and customType == :consumeType) " +
            "and (:paidType is not null and paidType == :paidType)" +
            "and createTime between :startTime and :endTime")
    fun getByCondition(startTime: Long, endTime: Long, consumeType: TransactionRecord.ConsumeType? = null,
                       paidType: TransactionRecord.PaidType? = null): List<TransactionRecord>

}