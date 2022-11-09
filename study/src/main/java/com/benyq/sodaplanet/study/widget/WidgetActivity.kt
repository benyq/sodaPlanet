package com.benyq.sodaplanet.study.widget

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.benyq.sodaplanet.study.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class WidgetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_widget)

//        val animator: ObjectAnimator = ObjectAnimator.ofFloat(
//            findViewById<TapeMeasureView>(R.id.tapeMeasureView), "currentScale", 70.4f, 80.1f
//        )
//        animator.duration = 2000
//        animator.start()
    }
}