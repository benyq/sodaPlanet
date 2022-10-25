package com.benyq.sodaplanet.transaction.ui.detail

import android.app.Application
import com.benyq.sodaplanet.base.ui.BaseViewModel
import com.benyq.sodaplanet.base.room.entity.TransactionRecord
import com.benyq.sodaplanet.base.room.sodaPlanetDB
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 *
 * @author benyq
 * @date 2022/10/14
 * @email 1520063035@qq.com
 *
 */
class TransactionRecordDetailViewModel(application: Application) : BaseViewModel(application) {

    private val _deleteRecordResult: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val deleteRecordResult: SharedFlow<Boolean> = _deleteRecordResult

    fun deleteRecord(record: TransactionRecord) {
        execute {
            sodaPlanetDB.transactionRecordDao().delete(record)
        }.onSuccess {
            _deleteRecordResult.emit(it > 0)
        }
    }
}