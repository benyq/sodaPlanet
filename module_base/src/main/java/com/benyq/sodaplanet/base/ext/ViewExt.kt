package com.benyq.sodaplanet.base.ext

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import java.util.concurrent.TimeUnit

/**
 *
 * @author benyq
 * @date 2022/10/11
 * @email 1520063035@qq.com
 *
 */


fun ImageView.load(any: Any, holderDrawable: Drawable? = null,
                   errorDrawable: Drawable? = null,
                   cache: Boolean? = true,
                   circle: Boolean? = false,){
    var options = RequestOptions()
        .placeholder(holderDrawable)
        .error(errorDrawable)

    cache?.isFalse {
        options = options.skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE)
    }
    circle?.isTrue {
        options = options.transform(CircleCrop())
    }

    Glide.with(this).load(any).apply(options).into(this)
}


fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.throttleClick(
    interval: Long = 500,
    unit: TimeUnit = TimeUnit.MILLISECONDS,
    block: View.() -> Unit
) {
    setOnClickListener(ThrottleClickListener(interval, unit, block))
}

class ThrottleClickListener(
    private val interval: Long = 500,
    private val unit: TimeUnit = TimeUnit.MILLISECONDS,
    private var block: View.() -> Unit
) : View.OnClickListener {
    private var lastTime: Long = 0

    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > unit.toMillis(interval)) {
            lastTime = currentTime
            block(v)
        }
    }
}

fun ViewPager2.overScrollNever() {
    val child: View = getChildAt(0)
    (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
}
