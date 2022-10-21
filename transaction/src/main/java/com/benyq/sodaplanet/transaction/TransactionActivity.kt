package com.benyq.sodaplanet.transaction

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.benyq.sodaplanet.base.ext.fullScreen
import com.benyq.sodaplanet.transaction.data.TransactionIntentExtra

class TransactionActivity : AppCompatActivity() {

    private var isFirstLaunch = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val result = findNavController(R.id.transaction_fragment).navigateUp()
                if (!result) finish()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        val shortcut = intent.getBooleanExtra(TransactionIntentExtra.shortcut, false)
        if (shortcut && isFirstLaunch) {
            isFirstLaunch = false
            findNavController(R.id.transaction_fragment).navigate(
                R.id.action_home_to_add, null, NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_bottom)
                    .setExitAnim(R.anim.anim_normal)
                    .setPopExitAnim(R.anim.slide_out_bottom)
                    .build()
            )
        }
    }
}