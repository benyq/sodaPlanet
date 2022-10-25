package com.benyq.sodaplanet.base.room.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 *
 * @author benyq
 * @date 2022/9/30
 * @email 1520063035@qq.com
 * 记账实体类
 */
@Entity(tableName = "transaction_record")
@Parcelize
data class TransactionRecord(
    //金额, 分为单位，1元 == 100分
    var amount: String,
    var consumeType: Int,
    var paidType: Int,
    var note: String,
    var createTime: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
): Parcelable
