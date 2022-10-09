package com.benyq.sodaplanet.transaction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        val tag = "TransactionFragment"

        supportFragmentManager.beginTransaction().let {
            val fragment = supportFragmentManager.findFragmentByTag(tag) ?: TransactionFragment().apply {
                it.add(R.id.flContainer, this, tag)
            }
            it.show(fragment)
            it.commitAllowingStateLoss()
        }
    }
}