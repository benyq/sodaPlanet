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
    enum class ConsumeType(val type: Int, val message: String, val order: Int) {
        SingleFood(0, "快餐", 1), TogetherFood(1, "聚餐", 2),
        FreshFood(2, "生鲜", 3), SuperMarket(3, "超市", 4),
        OnlineShopping(4, "网购", 5), Traffic(5, "交通", 6),
        HouseRent(6, "房租", 7), Entertainment(6, "娱乐", 4),
    }

    enum class PaidType(val type: Int, val message: String) {
        Cash(0, "现金"), CreditCard(1, "信用卡"), AliPay(2, "支付宝"), WechatPay(3, "微信")
    }
}

class TransactionConverters {
    @TypeConverter
    fun toConsumeType(type: Int): TransactionRecord.ConsumeType {
        return when (type) {
            0 -> TransactionRecord.ConsumeType.SingleFood
            1 -> TransactionRecord.ConsumeType.TogetherFood
            2 -> TransactionRecord.ConsumeType.FreshFood
            3 -> TransactionRecord.ConsumeType.SuperMarket
            4 -> TransactionRecord.ConsumeType.OnlineShopping
            5 -> TransactionRecord.ConsumeType.Traffic
            6 -> TransactionRecord.ConsumeType.HouseRent
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