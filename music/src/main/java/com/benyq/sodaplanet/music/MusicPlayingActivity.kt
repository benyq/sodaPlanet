package com.benyq.sodaplanet.music

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import com.benyq.sodaplanet.base.ext.appCtx
import com.benyq.sodaplanet.base.ext.getColorRef
import com.benyq.sodaplanet.base.ext.setStatusBarMode
import com.benyq.sodaplanet.base.ui.BaseActivity
import com.benyq.sodaplanet.music.databinding.ActivityMusicPlayingBinding
import com.benyq.sodaplanet.music.player.ExoMusicPlayer

class MusicPlayingActivity : BaseActivity<ActivityMusicPlayingBinding>() {

    override fun provideViewBinding() = ActivityMusicPlayingBinding.inflate(layoutInflater)

    private val exoMusicPlayer: ExoMusicPlayer by lazy {
        ExoMusicPlayer.getInstance()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        setStatusBarMode(getColorRef(R.color.dark_grey), true)

//        MusicNotificationHelper.showMusicNotification(this, false)

        binding.btnPlayLast.setOnClickListener {
            exoMusicPlayer.previousMusic()
        }

        binding.btnPlayNext.setOnClickListener {
//            exoMusicPlayer.nextMusic()
            appCtx.startService(Intent(appCtx, PlayMusicService::class.java))
        }

        binding.btnPlayToggle.setOnClickListener {
            if (exoMusicPlayer.isPlaying()) {
                binding.ivPlayToggle.setImageResource(R.drawable.ic_remote_view_pause)
                exoMusicPlayer.pause()
            }else {
                binding.ivPlayToggle.setImageResource(R.drawable.ic_remote_view_play)
                exoMusicPlayer.resume()
            }
        }


        binding.skMusic.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                exoMusicPlayer.seekTo((exoMusicPlayer.maxDuration() * binding.skMusic.progress * 1f / binding.skMusic.max).toLong())
            }
        })

        exoMusicPlayer.listener = { position: Long, buffer: Long ->
            binding.skMusic.secondaryProgress = (buffer * 100 / exoMusicPlayer.maxDuration()).toInt()
            binding.skMusic.progress = (position * 100 / exoMusicPlayer.maxDuration()).toInt()
        }
    }

}