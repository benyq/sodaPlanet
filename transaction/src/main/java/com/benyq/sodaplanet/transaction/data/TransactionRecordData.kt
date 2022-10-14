package com.benyq.sodaplanet.transaction.data

import com.benyq.sodaplanet.base.room.entity.TransactionRecord
import com.drake.brv.item.ItemExpand
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author benyq
 * @date 2022/10/13
 * @email 1520063035@qq.com
 *
 */
data class TransactionRecordData(val record: TransactionRecord): ItemExpand {

    override var itemExpand: Boolean = false
    override var itemGroupPosition: Int = 0
    override var itemSublist: List<Any?>? = null

    val consumeType: ConsumeType = ConsumeType.fromCode(record.consumeType)
    val paidType: PaidType = PaidType.fromCode(record.paidType)

    fun date(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        return dateFormat.format(record.createTime)
    }

}