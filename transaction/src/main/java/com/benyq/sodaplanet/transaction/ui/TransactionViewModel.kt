package com.benyq.sodaplanet.transaction.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.benyq.sodaplanet.base.room.entity.TransactionRecord
import com.benyq.sodaplanet.base.room.sodaPlanetDB

/**
 * @author benyq
 * @email 1520063035@qq.com
 * @date 2022/10/5
 */
class TransactionViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    var currentIndex: Int
        set(value) {
            savedStateHandle.set("currentIndex", value)
        }
        get() {
            return savedStateHandle.get<Int>("currentIndex") ?: 0
        }

}