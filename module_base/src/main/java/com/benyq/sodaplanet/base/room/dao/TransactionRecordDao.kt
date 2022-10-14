package com.benyq.sodaplanet.base.room.dao

import androidx.room.*
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
    fun addTransactionRecord(record: TransactionRecord): Long


    @Query("select * from transaction_record where (:consumeType is null or consumeType == :consumeType) " +
            "and (:paidType is null or paidType == :paidType)" +
            "and createTime between :startTime and :endTime order by createTime desc")
    fun getByCondition(startTime: Long, endTime: Long, consumeType: Int? = null,
                       paidType: Int? = null): List<TransactionRecord>

    @Delete
    fun delete(record: TransactionRecord): Int

    @Update
    fun update(record: TransactionRecord): Int

}