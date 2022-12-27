package com.benyq.sodaplanet.music

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.benyq.sodaplanet.base.coroutine.Coroutine
import com.benyq.sodaplanet.music.player.ExoMusicPlayer
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlin.coroutines.CoroutineContext

class PlayMusicService : Service(), CoroutineScope by MainScope() {
    override fun onBind(intent: Intent) = null

    companion object {
        const val ACTION_PLAY_PLAY = "com.benyq.sodaplanet.music.ACTION.PLAY_PLAY"
        const val ACTION_PLAY_PAUSE = "com.benyq.sodaplanet.music.ACTION.PLAY_PAUSE"
        const val ACTION_PLAY_RESUME = "com.benyq.sodaplanet.music.ACTION.PLAY_RESUME"
        const val ACTION_PLAY_PREVIOUS = "com.benyq.sodaplanet.music.ACTION.PLAY_PREVIOUS"
        const val ACTION_PLAY_NEXT = "com.benyq.sodaplanet.music.ACTION.PLAY_NEXT"


        const val ACTION_PLAY_TOGGLE = "com.benyq.sodaplanet.music.ACTION.PLAY_TOGGLE"
        const val ACTION_STOP_SERVICE = "com.benyq.sodaplanet.music.ACTION.STOP_SERVICE"
    }

    override fun onCreate() {
        super.onCreate()
        Logger.d("service onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val isPlaying = ExoMusicPlayer.getInstance().isPlaying()
//        MusicNotificationHelper.showMusicNotification(this, isPlaying)
        upNotification(isPlaying)
        Logger.d("service onStartCommand")
        intent?.action?.let {
            Logger.d("service action: $it")
            when(it)  {
                ACTION_PLAY_PLAY -> ExoMusicPlayer.getInstance().play()
                ACTION_PLAY_NEXT -> ExoMusicPlayer.getInstance().nextMusic()
                ACTION_PLAY_PREVIOUS -> ExoMusicPlayer.getInstance().previousMusic()
                ACTION_PLAY_PAUSE -> ExoMusicPlayer.getInstance().pause()
                ACTION_PLAY_RESUME -> ExoMusicPlayer.getInstance().resume()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun <T> execute(
        scope: CoroutineScope = this,
        context: CoroutineContext = Dispatchers.IO,
        block: suspend CoroutineScope.() -> T
    ) = Coroutine.async(scope, context) { block() }

    private fun upNotification(pause: Boolean) {
        execute {
            val nSubtitle = "nSubtitle"
            val builder = NotificationCompat
                .Builder(this@PlayMusicService, "channelIdMusic")
                .setSmallIcon(R.drawable.ic_practice)
                .setSubText("Audio")
                .setOngoing(true)
                .setContentTitle("nTitle")
                .setContentText(nSubtitle)
                .setContentIntent(
                    activityPendingIntent<MusicPlayingActivity>("activity")
                )
            builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_launcher))

            builder.addAction(
                R.drawable.ic_remote_view_play_last,
                "",
                servicePendingIntent<PlayMusicService>(ACTION_PLAY_PREVIOUS)
            )

            if (pause) {
                builder.addAction(
                    R.drawable.ic_remote_view_pause,
                    "继续",
                    servicePendingIntent<PlayMusicService>(ACTION_PLAY_PLAY)
                )
            } else {
                builder.addAction(
                    R.drawable.ic_remote_view_play,
                    "暂停",
                    servicePendingIntent<PlayMusicService>(ACTION_PLAY_PAUSE)
                )
            }
            builder.addAction(
                R.drawable.ic_remote_view_play_next,
                "",
                servicePendingIntent<PlayMusicService>(ACTION_PLAY_NEXT)
            )

            builder.setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
            )
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            builder
        }.onSuccess {
            startForeground(11, it.build())
        }
    }

}

@SuppressLint("UnspecifiedImmutableFlag")
inline fun <reified T : Activity> Context.activityPendingIntent(
    action: String,
    configIntent: Intent.() -> Unit = {}
): PendingIntent? {
    val intent = Intent(this, T::class.java)
    intent.action = action
    configIntent.invoke(intent)
    val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
    return PendingIntent.getActivity(this, 0, intent, flags)
}
