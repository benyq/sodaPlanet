package com.benyq.sodaplanet.transaction.ui.detail

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.benyq.sodaplanet.base.ext.setStatusBarMode
import com.benyq.sodaplanet.base.room.entity.TransactionRecord
import com.benyq.sodaplanet.transaction.R
import com.benyq.sodaplanet.transaction.data.TransactionIntentExtra

/**
 * @author benyq
 * @date 2022/10/14
 * @email 1520063035@qq.com
 */
class TransactionRecordDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_record_detail)
        setStatusBarMode(Color.WHITE, true)
    }

    override fun onStart() {
        super.onStart()

        val record: TransactionRecord = intent.getParcelableExtra(TransactionIntentExtra.transactionRecord)!!
        findNavController(R.id.transaction_fragment).setGraph(R.navigation.nav_transaction_detail, Bundle().apply {
            putParcelable(TransactionIntentExtra.transactionRecord, record)
        })
    }

}