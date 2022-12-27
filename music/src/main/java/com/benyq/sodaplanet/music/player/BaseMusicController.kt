package com.benyq.sodaplanet.music.player

import android.content.Context
import com.benyq.sodaplanet.music.DataCenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 *
 * @author benyq
 * @date 2022/11/30
 * @email 1520063035@qq.com
 * 音乐控制器接口
 */
abstract class BaseMusicController(context: Context, protected val dataCenter: DataCenter) :
    CoroutineScope by MainScope() {

    protected val mContext: Context = context.applicationContext

    abstract fun play()
    abstract fun stop()

    abstract fun pause()
    abstract fun resume()

    abstract fun release()

    abstract fun seekTo(progress: Long)

    abstract fun duration(): Long

    abstract fun maxDuration(): Long

    abstract fun nextMusic()

    abstract fun previousMusic()

    abstract fun isPlaying(): Boolean

}

