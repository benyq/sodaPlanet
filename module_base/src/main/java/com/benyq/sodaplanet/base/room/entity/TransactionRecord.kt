package com.benyq.sodaplanet.base.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

/**
 *
 * @author benyq
 * @date 2022/9/30
 * @email 1520063035@qq.com
 * 记账实体类
 */
@Entity(tableName = "transaction_record")
data class TransactionRecord(
    //金额, 分为单位，1元 == 100分
    var amount: Int,
    @TypeConverters(TransactionConverters::class)
    var customType: ConsumeType,
    @TypeConverters(TransactionConverters::class)
    var paidType: PaidType,
    val createTime: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
) {
    enum class ConsumeType(val type: Int) {
        Food(0), Traffic(1), HouseRent(2)
    }

    enum class PaidType(val type: Int) {
        Cash(0), CreditCard(1), AliPay(2), WechatPay(3)
    }
}

class TransactionConverters {
    @TypeConverter
    fun toConsumeType(type: Int): TransactionRecord.ConsumeType {
        return when (type) {
            0 -> TransactionRecord.ConsumeType.Food
            1 -> TransactionRecord.ConsumeType.Traffic
            2 -> TransactionRecord.ConsumeType.HouseRent
            else -> throw IllegalArgumentException("Could not recognize ConsumeType")
        }
    }

    @TypeConverter
    fun fromConsumeType(type: TransactionRecord.ConsumeType) = type.type

    @TypeConverter
    fun toPaidType(type: Int): TransactionRecord.PaidType {
        return when (type) {
            0 -> TransactionRecord.PaidType.Cash
            1 -> TransactionRecord.PaidType.CreditCard
            2 -> TransactionRecord.PaidType.AliPay
            3 -> TransactionRecord.PaidType.WechatPay
            else -> throw IllegalArgumentException("Could not recognize PaidType")
        }
    }

    @TypeConverter
    fun fromPaidType(type: TransactionRecord.PaidType) = type.type
}