package com.benyq.sodaplanet.transaction.data

import com.benyq.sodaplanet.base.room.entity.TransactionRecord
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author benyq
 * @date 2022/10/19
 * @email 1520063035@qq.com
 * 对于 TransactionEntity 的封装
 */

data class TransactionDateData(val date: String, var amount: Float, val order: Int)

data class TransactionConsumeData(val record: TransactionRecord, var ratio: Float) {

    val consumeType: ConsumeType = ConsumeType.fromCode(record.consumeType)
    val paidType: PaidType = PaidType.fromCode(record.paidType)

    fun date(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        return dateFormat.format(record.createTime)
    }

    val radioContent: String
        get() = "支出占比: ${"%.2f".format(ratio * 100)}%"
}