package com.benyq.sodaplanet.transaction.ui.add

import android.app.Application
import com.benyq.sodaplanet.base.base.BaseViewModel
import com.benyq.sodaplanet.base.room.entity.TransactionRecord
import com.benyq.sodaplanet.base.room.sodaPlanetDB
import com.benyq.sodaplanet.transaction.data.ConsumeType
import com.benyq.sodaplanet.transaction.data.PaidType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 *
 * @author benyq
 * @date 2022/10/11
 * @email 1520063035@qq.com
 *
 */
class TransactionAddRecordViewModel(application: Application) : BaseViewModel(application) {

    private val _addRecordResult: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val addRecordResult: SharedFlow<Boolean> = _addRecordResult

    fun addTransactionRecord(amount: String, consumeType: ConsumeType, paidType: PaidType, note: String) {
        val amountCent = yuan2fen(amount)

        execute {
            sodaPlanetDB.transactionRecordDao().addTransactionRecord(TransactionRecord(amountCent, consumeType.code, paidType.code, note))
        }.onSuccess {
            _addRecordResult.emit(it > 0)
        }

    }

    fun updateTransactionRecord(record: TransactionRecord, amount: String) {
        record.amount = yuan2fen(amount)
        execute {
            sodaPlanetDB.transactionRecordDao().update(record)
        }.onSuccess {
            record.amount = amount
            _addRecordResult.emit(it > 0)
        }
    }

    private fun yuan2fen(amount: String): String {
        return if (amount.contains(".")) {
            amount.replace(".", "")
        }else {
            "${amount}00"
        }
    }
}