package com.benyq.sodaplanet.transaction.data

import com.benyq.sodaplanet.base.ext.toNumberDefault
import com.benyq.sodaplanet.base.room.entity.TransactionRecord
import com.drake.brv.item.ItemExpand
import com.orhanobut.logger.Logger
import java.util.*

/**
 *
 * @author benyq
 * @date 2022/10/12
 * @email 1520063035@qq.com
 *
 */
class TransactionGroupRecordData(var tag: String = "") : ItemExpand {

    private val calendar = Calendar.getInstance()

    init {
        calendar.firstDayOfWeek = Calendar.MONDAY
    }

    override var itemExpand: Boolean = true
    override var itemGroupPosition: Int = 0
    override var itemSublist: List<Any?>?
        get() = finalList
        set(value) {
            finalList.clear()
            finalList.addAll(value as List<TransactionRecordData>)

        }

    val finalList = mutableListOf<TransactionRecordData>()

    fun setTime(time: Long) {
        calendar.timeInMillis = time
    }

    fun totalAmount(): String {
        var sum = 0f
        finalList.forEach {
            sum += it.record.amount.toNumberDefault(0f)
        }
        return  "%.2f".format(sum)
    }

    fun date(): String {
        return "${calendar.get(Calendar.MONTH) + 1}月${calendar.get(Calendar.DAY_OF_MONTH)}日"
    }

    fun week(): String {
        return int2Week(calendar.get(Calendar.DAY_OF_WEEK))
    }


    private fun int2Week(number: Int): String {
        Logger.d("week number: $number")
        var dayOfWeek: Int = number - 1
        if (dayOfWeek == 0) {
            dayOfWeek = 7
        }
        val week = listOf("一", "二", "三", "四", "五", "六", "七")
        return "星期" + week[dayOfWeek - 1]
    }
}