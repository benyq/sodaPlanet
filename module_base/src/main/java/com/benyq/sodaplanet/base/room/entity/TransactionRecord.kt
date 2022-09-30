package com.benyq.sodaplanet.base.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
 * @author benyq
 * @date 2022/9/30
 * @email 1520063035@qq.com
 * 记账实体类
 */
@Entity(tableName = "transaction_record")
data class TransactionRecord(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)