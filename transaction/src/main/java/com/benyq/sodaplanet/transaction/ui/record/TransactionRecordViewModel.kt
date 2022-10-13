package com.benyq.sodaplanet.transaction.ui.record

import android.app.Application
import com.benyq.sodaplanet.base.base.BaseViewModel
import com.benyq.sodaplanet.base.ext.toNumberDefault
import com.benyq.sodaplanet.base.room.sodaPlanetDB
import com.benyq.sodaplanet.transaction.data.TransactionGroupRecordData
import com.benyq.sodaplanet.transaction.data.TransactionRecordData
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author benyq
 * @date 2022/10/12
 * @email 1520063035@qq.com
 *
 */
class TransactionRecordViewModel(application: Application) : BaseViewModel(application) {

    private val _transactionRecords: MutableSharedFlow<List<TransactionGroupRecordData>> = MutableSharedFlow()
    val transactionRecords: SharedFlow<List<TransactionGroupRecordData>> = _transactionRecords

    private val _transactionRecordAmountSum: MutableSharedFlow<String> = MutableSharedFlow()
    val transactionRecordAmountSum: SharedFlow<String> = _transactionRecordAmountSum

    fun getRecordTime(startTime: Long, endTime: Long) {
        execute {
            var sum = 0f
            val records = sodaPlanetDB.transactionRecordDao().getByCondition(startTime, endTime).map {
                    record->
                record.amount = fen2yuan(record.amount)
                sum += record.amount.toNumberDefault(0f)
                record
            }

            val groupRecords = mutableListOf<TransactionGroupRecordData>()
            var currentGroupRecord = TransactionGroupRecordData()
            records.forEach { record->
                val tag = SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(Date(record.createTime))
                Logger.d("tag: $tag, time: ${record.createTime}")
                if (currentGroupRecord.tag == tag) {
                    currentGroupRecord.finalList.add(TransactionRecordData(record))
                }else {
                    currentGroupRecord = TransactionGroupRecordData(tag)
                    currentGroupRecord.setTime(record.createTime)
                    groupRecords.add(currentGroupRecord)
                    currentGroupRecord.finalList.add(TransactionRecordData(record))
                }
            }
            Pair(groupRecords, "%.2f".format(sum))
        }.onSuccess {
            Logger.d(it)
            _transactionRecords.emit(it.first)
            _transactionRecordAmountSum.emit(it.second)
        }
    }


    private fun fen2yuan(amount: String): String {
        val part1 = amount.substring(0, amount.length - 2)
        val part2 = amount.substring(amount.length - 2, amount.length )
        return "${part1}.${part2}"
    }
}