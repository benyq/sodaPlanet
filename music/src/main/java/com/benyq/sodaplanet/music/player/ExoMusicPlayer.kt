package com.benyq.sodaplanet.music.player

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.benyq.sodaplanet.base.ext.appCtx
import com.benyq.sodaplanet.music.DataCenter
import com.benyq.sodaplanet.music.PlayMusicService
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.cache.*
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit

/**
 *
 * @author benyq
 * @date 2022/11/30
 * @email 1520063035@qq.com
 *
 */
@SuppressLint("StaticFieldLeak")
class ExoMusicPlayer private constructor(context: Context, dataCenter: DataCenter) :
    BaseMusicController(context, dataCenter), Player.Listener {

    companion object {
        @Volatile
        private var INSTANCE: ExoMusicPlayer? = null

        @JvmStatic
        fun getInstance(): ExoMusicPlayer {
            if (INSTANCE == null) {
                synchronized(this) {
                    if (INSTANCE == null) {
                        INSTANCE = ExoMusicPlayer(appCtx, DataCenter())
                    }
                }
            }
            return INSTANCE!!
        }
    }

    @Volatile
    private var started: Boolean = false

    private var upPlayProgressJob: Job? = null

    var listener: ((Long, Long)->Unit)? = null

    private val exoplayer: ExoPlayer by lazy {
        ExoPlayer.Builder(context)
            .build().apply {
                addListener(this@ExoMusicPlayer)
            }
    }

    override fun play() {
        playNewMusic(dataCenter.current)
    }

    override fun stop() {
        exoplayer.stop()
    }

    override fun resume() {
        exoplayer.play()
        notifyService(true)
    }

    override fun pause() {
        exoplayer.pause()
    }

    override fun release() {
        notifyService(false)
        cache.release()
        exoplayer.release()
        cancel()
    }

    override fun seekTo(progress: Long) {
        exoplayer.seekTo(progress)
    }

    override fun duration(): Long {
        return exoplayer.currentPosition
    }

    override fun maxDuration(): Long {
        return exoplayer.duration
    }

    override fun nextMusic() {

        stop()

        playNewMusic(dataCenter.next)
    }

    override fun previousMusic() {
        stop()

        playNewMusic(dataCenter.previous)
    }

    override fun isPlaying() = exoplayer.isPlaying


    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            Player.STATE_IDLE -> {
                // 空闲
            }
            Player.STATE_BUFFERING -> {
                // 缓冲中
            }
            Player.STATE_READY -> {
                upPlayProgress()
            }
            Player.STATE_ENDED -> {
                nextMusic()
            }
        }
    }

    override fun onPlayerError(error: PlaybackException) {

    }

    private fun upPlayProgress() {
        upPlayProgressJob?.cancel()
        upPlayProgressJob = launch {
            while (isActive) {
                listener?.invoke(exoplayer.currentPosition, exoplayer.bufferedPosition)
                delay(1000)
            }
        }
    }

    private fun playNewMusic(url: String) {
        if (url.startsWith("http")) {
            val mediaItem: MediaItem = MediaItem.fromUri(url)
            val mediaSourceFactory = ProgressiveMediaSource.Factory(
                cacheDataSourceFactory
            )
            exoplayer.setMediaSource(mediaSourceFactory.createMediaSource(mediaItem))
        } else {
            val mediaItem: MediaItem = MediaItem.fromUri(url)
            exoplayer.setMediaItem(mediaItem)
        }
        exoplayer.setPlaybackSpeed(1.2f)
        exoplayer.playWhenReady = true
        val start = System.currentTimeMillis()
        exoplayer.prepare()
        Logger.d("cost time: ${System.currentTimeMillis() - start}")
        notifyService(true)
        Logger.d("notifyService")
    }

    private fun notifyService(bool: Boolean) {
        if (bool) {
            appCtx.startService(Intent(appCtx, PlayMusicService::class.java))
        }else {
            appCtx.stopService(Intent(appCtx, PlayMusicService::class.java))
        }
    }

    /**
     * Okhttp DataSource.Factory
     */
    private val okhttpDataFactory by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .callTimeout(60, TimeUnit.SECONDS)
        OkHttpDataSource.Factory(builder.build())
            .setCacheControl(CacheControl.Builder().maxAge(1, TimeUnit.DAYS).build())
    }


    /**
     * 支持缓存的DataSource.Factory
     */
    private val cacheDataSourceFactory by lazy {
        //使用自定义的CacheDataSource以支持设置UA
        return@lazy CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(okhttpDataFactory)
            .setCacheReadDataSourceFactory(FileDataSource.Factory())
            .setCacheWriteDataSinkFactory(
                CacheDataSink.Factory()
                    .setCache(cache)
                    .setFragmentSize(CacheDataSink.DEFAULT_FRAGMENT_SIZE)
            )
    }

    /**
     * Exoplayer 内置的缓存
     */
    private val cache: Cache by lazy {
        val databaseProvider = StandaloneDatabaseProvider(context)
        return@lazy SimpleCache(
            //Exoplayer的缓存路径
            File(context.externalCacheDir, "exoplayer"),
            //100M的缓存
            LeastRecentlyUsedCacheEvictor((100 * 1024 * 1024).toLong()),
            //记录缓存的数据库
            databaseProvider
        )
    }
}