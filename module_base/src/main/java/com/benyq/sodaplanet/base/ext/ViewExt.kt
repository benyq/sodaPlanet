package com.benyq.sodaplanet.base.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions

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