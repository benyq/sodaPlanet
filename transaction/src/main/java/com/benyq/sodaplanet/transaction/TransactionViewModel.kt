package com.benyq.sodaplanet.transaction

import androidx.lifecycle.ViewModel
import com.benyq.sodaplanet.base.room.entity.TransactionRecord
import com.benyq.sodaplanet.base.room.sodaPlanetDB

/**
 * @author benyq
 * @email 1520063035@qq.com
 * @date 2022/10/5
 */
class TransactionViewModel : ViewModel() {


    private fun getTransactionByCondition(
        startTime: Long, endTime: Long,
        consumeType: TransactionRecord.ConsumeType? = null,
        paidType: TransactionRecord.PaidType? = null,
    ): List<TransactionRecord> {
        return sodaPlanetDB.transactionRecordDao().getByCondition(startTime, endTime, consumeType, paidType)
    }
}