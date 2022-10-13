package com.benyq.sodaplanet.transaction.data

import com.benyq.sodaplanet.base.room.entity.TransactionRecord
import com.drake.brv.item.ItemExpand

/**
 *
 * @author benyq
 * @date 2022/10/13
 * @email 1520063035@qq.com
 *
 */
data class TransactionRecordData(val record: TransactionRecord): ItemExpand {

    val consumeType: ConsumeType = ConsumeType.fromCode(record.customType)
    val paidType: PaidType = PaidType.fromCode(record.paidType)

    override var itemExpand: Boolean = false
    override var itemGroupPosition: Int = 0
    override var itemSublist: List<Any?>? = null

}