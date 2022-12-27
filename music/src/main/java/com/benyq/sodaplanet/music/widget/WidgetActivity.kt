package com.benyq.sodaplanet.music.widget

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.benyq.sodaplanet.music.R

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