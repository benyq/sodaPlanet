package com.benyq.sodaplanet.music

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.benyq.sodaplanet.base.ext.fromO
import com.benyq.sodaplanet.base.ext.fromS
import com.benyq.sodaplanet.music.R

/**
 *
 * @author benyq
 * @date 2022/12/7
 * @email 1520063035@qq.com
 *
 */
object MusicNotificationHelper {

    private const val MUSIC_CHANNEL_ID = "channelIdMusic"
    private const val MUSIC_CHANNEL_NAME = "Music通知"
    private const val musicNotify = 11


    private lateinit var mNotificationManager : NotificationManager

    private var mContentViewSmall: RemoteViews? = null


    fun init(context: Context) {
        mNotificationManager = context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (fromO()) {
            val notificationChannel =
                NotificationChannel(
                    MUSIC_CHANNEL_ID,
                    MUSIC_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，
            //通知才能正常弹出
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun showMusicNotification(context: Service, isPlaying: Boolean) {
        val contentIntent = PendingIntent.getActivity(context, musicNotify, Intent(context, MusicPlayingActivity::class.java), PendingIntent.FLAG_CANCEL_CURRENT)
        if (fromO()) {
            val notificationChannel =
                NotificationChannel(
                    MUSIC_CHANNEL_ID,
                    MUSIC_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，
            //通知才能正常弹出
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
        val notification: Notification = NotificationCompat.Builder(context, MUSIC_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_practice) // the status icon
            .setWhen(System.currentTimeMillis()) // the time stamp
            .setCustomContentView(getMusicView(context, isPlaying))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(contentIntent)
            .build()

        context.startForeground(musicNotify, notification)
    }

    private fun getMusicView(context: Context, isPlaying: Boolean) : RemoteViews {
        mContentViewSmall = mContentViewSmall ?: RemoteViews(
            context.packageName,
            R.layout.remote_view_player_small
        ).apply {
            setUpRemoteView(this, context)
        }
        updateRemoteView(mContentViewSmall!!, isPlaying)
        return mContentViewSmall!!
    }

    private fun updateRemoteView(remoteView: RemoteViews, isPlaying: Boolean) {
        remoteView.setImageViewResource(
            R.id.image_view_play_toggle,
            if (isPlaying) R.drawable.ic_remote_view_play else R.drawable.ic_remote_view_pause
        )
    }

    private fun setUpRemoteView(remoteView: RemoteViews, context: Context) {
        remoteView.setImageViewResource(R.id.image_view_close, R.drawable.ic_remote_view_close)
        remoteView.setImageViewResource(
            R.id.image_view_play_last,
            R.drawable.ic_remote_view_play_last
        )
        remoteView.setImageViewResource(
            R.id.image_view_play_next,
            R.drawable.ic_remote_view_play_next
        )

        remoteView.setOnClickPendingIntent(
            R.id.button_play_last,
//            getPendingIntent(PlayMusicService.ACTION_PLAY_LAST, context)
                    context.servicePendingIntent<PlayMusicService>(PlayMusicService.ACTION_PLAY_NEXT)
        )
        remoteView.setOnClickPendingIntent(
            R.id.button_play_next,
            getPendingIntent(PlayMusicService.ACTION_PLAY_NEXT, context)
        )
        remoteView.setOnClickPendingIntent(
            R.id.button_play_toggle,
            getPendingIntent(PlayMusicService.ACTION_PLAY_TOGGLE, context)
        )
        remoteView.setOnClickPendingIntent(R.id.button_close, getPendingIntent(PlayMusicService.ACTION_STOP_SERVICE, context))
    }

    private fun getPendingIntent(action: String, context: Context): PendingIntent {

        val flags = if (fromS()) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getService(context, 0, Intent(action).setPackage(context.packageName), flags)
    }

}

inline fun <reified T : Service> Context.servicePendingIntent(
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
    return PendingIntent.getService(this, 0, intent, flags)
}