package com.benyq.sodaplanet.transaction.ui.analysis

import android.app.Application
import com.benyq.sodaplanet.base.ui.BaseViewModel
import com.benyq.sodaplanet.base.ext.toNumberDefault
import com.benyq.sodaplanet.base.room.sodaPlanetDB
import com.benyq.sodaplanet.transaction.data.TransactionConsumeData
import com.benyq.sodaplanet.transaction.data.TransactionDateData
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author benyq
 * @date 2022/10/19
 * @email 1520063035@qq.com
 *
 */
class TransactionAnalysisViewModel(application: Application): BaseViewModel(application) {

    private val _dateTransactionData: MutableSharedFlow<Pair<List<TransactionDateData>, Float>> = MutableSharedFlow()
    val dateTransactionData: SharedFlow<Pair<List<TransactionDateData>, Float>> = _dateTransactionData

    private val _consumeTransactionData: MutableSharedFlow<List<TransactionConsumeData>> = MutableSharedFlow()
    val consumeTransactionData: SharedFlow<List<TransactionConsumeData>> = _consumeTransactionData


    fun getWeekData() {
        /**
         * 获取日期所在周的第一天和最后一天
         */
        val sdf = SimpleDateFormat("MM-dd", Locale.CHINA)
        val emptyData = mutableListOf<TransactionDateData>()

        val calendarWeek = Calendar.getInstance()
        calendarWeek.time = Date()
        calendarWeek.add(Calendar.DATE, 0 * 7) // 0 表示当前周，-1 表示上周，1 表示下周，以此类推
        calendarWeek.firstDayOfWeek = Calendar.MONDAY //以周一为首日

        calendarWeek.set(Calendar.MINUTE, 0)
        calendarWeek.set(Calendar.SECOND, 0)
        calendarWeek.set(Calendar.MILLISECOND, 0)
        calendarWeek.set(Calendar.HOUR_OF_DAY, 0)


        calendarWeek[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        val start = calendarWeek.timeInMillis
        emptyData.add(TransactionDateData(sdf.format(start), 0f, 1))


        for (d in Calendar.TUESDAY .. Calendar.SATURDAY) {
            calendarWeek[Calendar.DAY_OF_WEEK] = d
            emptyData.add(TransactionDateData(sdf.format(calendarWeek.time), 0f, d - 1))
        }

        calendarWeek[Calendar.DAY_OF_WEEK] = Calendar.SUNDAY
        emptyData.add(TransactionDateData(sdf.format(calendarWeek.timeInMillis), 0f, 7))

        calendarWeek.set(Calendar.HOUR_OF_DAY, 24)

        val end = calendarWeek.timeInMillis



        getDateRecords(start, end, "MM-dd", emptyData)
        getConsumeTypeData(start, end)
    }

    fun getMonthData() {
        /**
         * 获取日期所在月度的第一天和最后一天
         */
        val sdf = SimpleDateFormat("dd", Locale.CHINA)
        val emptyData = mutableListOf<TransactionDateData>()

        val calendarMonth = Calendar.getInstance()
        calendarMonth.time = Date() //修改成需要的日期
        calendarMonth.set(Calendar.MINUTE, 0)
        calendarMonth.set(Calendar.SECOND, 0)
        calendarMonth.set(Calendar.MILLISECOND, 0)
        calendarMonth.set(Calendar.HOUR_OF_DAY, 0)

        calendarMonth.add(Calendar.MONTH, 0) // -1表示上个月，0表示本月，1表示下个月，上下月份以此类型

        for (d in calendarMonth.getActualMinimum(Calendar.DAY_OF_MONTH) .. calendarMonth.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            calendarMonth[Calendar.DAY_OF_MONTH] = d
            emptyData.add(TransactionDateData(sdf.format(calendarMonth.time), 0f, d))
        }

        calendarMonth[Calendar.DAY_OF_MONTH] = calendarMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
        val start = calendarMonth.timeInMillis

        calendarMonth[Calendar.DAY_OF_MONTH] = calendarMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendarMonth.set(Calendar.HOUR_OF_DAY, 24)

        val end = calendarMonth.timeInMillis
        getDateRecords(start, end, "dd", emptyData)
        getConsumeTypeData(start, end)

    }

    fun getYearData() {
        /**
         * 获取日期所在年度的第一天和最后一天
         */
        val sdf = SimpleDateFormat("MM", Locale.CHINA)
        val emptyData = mutableListOf<TransactionDateData>()

        val calendarYear = Calendar.getInstance()
        calendarYear.time = Date() //修改成需要的日期
        calendarYear.set(Calendar.MINUTE, 0)
        calendarYear.set(Calendar.SECOND, 0)
        calendarYear.set(Calendar.MILLISECOND, 0)
        calendarYear.set(Calendar.HOUR_OF_DAY, 0)

        calendarYear.add(Calendar.YEAR, 0) // -1表示上个月，0表示本月，1表示下个月，上下月份以此类型

        for (d in 0 .. 11) {
            calendarYear[Calendar.MONTH] = d
            emptyData.add(TransactionDateData(sdf.format(calendarYear.time), 0f, d))
        }

        calendarYear[Calendar.DAY_OF_YEAR] = calendarYear.getActualMinimum(Calendar.DAY_OF_YEAR)
        val start = calendarYear.timeInMillis

        calendarYear[Calendar.DAY_OF_YEAR] = calendarYear.getActualMaximum(Calendar.DAY_OF_YEAR)
        calendarYear.set(Calendar.HOUR_OF_DAY, 24)

        val end = calendarYear.timeInMillis
        getDateRecords(start, end, "MM", emptyData)
        getConsumeTypeData(start, end)

    }


    private fun getDateRecords(startTime: Long, endTime: Long, format: String, emptyData: List<TransactionDateData>) {
        Logger.d("getDateRecords: startTime: $startTime, endTime: $endTime, format: $format, emptyData: $emptyData")
        execute {
            val sdf = SimpleDateFormat(format, Locale.CHINA)
            val data = sodaPlanetDB.transactionRecordDao().getByCondition(startTime, endTime).map {
                it.amount = fen2yuan(it.amount)
                it
            }.groupBy { sdf.format(it.createTime) }

            var totalAmount = 0f
            data.forEach { kv->
                kv.value.forEach { record->
                    totalAmount += record.amount.toNumberDefault(0f)
                }
                emptyData.find { it.date == kv.key }?.amount = totalAmount
            }
            Pair(emptyData, totalAmount)
        }.onError {
            Logger.e("getDateRecords error: $it")
        }.onSuccess {
            _dateTransactionData.emit(it)
        }
    }

    private fun getConsumeTypeData(startTime: Long, endTime: Long) {
        execute {
            var totalAmount = 0f
            val data = sodaPlanetDB.transactionRecordDao().getByConsume(startTime, endTime).map {
                it.amount = fen2yuan(it.amount)
                totalAmount += it.amount.toNumberDefault(0f)
                TransactionConsumeData(it, 0f)
            }
            data.forEach {
                it.ratio = it.record.amount.toNumberDefault(0f) / totalAmount
            }
            data.sortedByDescending { it.ratio }
        }.onError {
            Logger.e("getConsumeTypeData error: $it")
        }.onSuccess {
            _consumeTransactionData.emit(it)
        }
    }

    private fun fen2yuan(amount: String): String {
        val part1 = amount.substring(0, amount.length - 2)
        val part2 = amount.substring(amount.length - 2, amount.length )
        return "${part1}.${part2}"
    }
}