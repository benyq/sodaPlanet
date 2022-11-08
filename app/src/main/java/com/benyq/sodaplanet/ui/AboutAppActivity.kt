package com.benyq.sodaplanet.ui

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.benyq.sodaplanet.net.entity.ApkVersionEntity
import com.benyq.sodaplanet.BuildConfig
import com.benyq.sodaplanet.base.ext.launchAndRepeatWithViewLifecycle
import com.benyq.sodaplanet.base.ext.toast
import com.benyq.sodaplanet.base.ui.BaseActivity
import com.benyq.sodaplanet.base.ui.WebViewActivity
import com.benyq.sodaplanet.databinding.ActivityAboutAppBinding
import com.orhanobut.logger.Logger
import kotlinx.coroutines.*
import java.io.File

class AboutAppActivity : BaseActivity<ActivityAboutAppBinding>() {

    private var apkReceiver: BroadcastReceiver? = null
    private var downloadJob: Job? = null

    private val vm by viewModels<AboutAppViewModel>()

    override fun provideViewBinding() = ActivityAboutAppBinding.inflate(layoutInflater)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val content = "版本号: ${BuildConfig.VERSION_NAME}"
        binding.tvVersionName.text = content

        binding.icAppUrl.setOnClickListener {
            WebViewActivity.gotoWeb(this, "https://github.com/benyq/sodaPlanet", "苏打星球")
        }

        binding.icUpdateCheck.setOnClickListener {
            vm.checkApkVersion()
        }
        binding.headerView.setBackAction { finish() }
        launchAndRepeatWithViewLifecycle {
            vm.apkVersion.collect {
                if (it.version > BuildConfig.VERSION_CODE) {
                    downloadApk(it)
                }else {
                    toast("当前已经是最新版本")
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        apkReceiver?.let { unregisterReceiver(it) }

    }

    private fun downloadApk(data: ApkVersionEntity) {

        if (downloadJob != null && downloadJob!!.isActive) return

        binding.icUpdateCheck.setContent("")

        apkReceiver?.let { unregisterReceiver(it) }

        val downloadUrl = assets.open("net").bufferedReader().readText() + data.url
        Logger.d("downloadUrl: $downloadUrl")
        val fileName = downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1)
        //这里下载到指定的目录，我们存在公共目录下的download文件夹下
        val fileUri = Uri.fromFile(
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                System.currentTimeMillis().toString() + "-" + fileName)
        )
        //开始构建 DownloadRequest 对象
        val request = DownloadManager.Request(Uri.parse(downloadUrl))

        //构建通知栏样式
        request.setTitle("苏打星球")
        request.setDescription("下载最新版本Apk")

        //下载或下载完成的时候显示通知栏
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE or DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

        //下载时候隐藏通知栏
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)

        //指定下载的文件类型为APK
        request.setMimeType("application/vnd.android.package-archive")
        //指定下载到本地的路径(可以指定URI)
        request.setDestinationUri(fileUri)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        //加入Request到系统下载队列，在条件满足时会自动开始下载。返回的为下载任务的唯一ID
        val requestID = downloadManager.enqueue(request)
        Logger.d("开始下载：fileUri:$fileUri requestID:$requestID")

        downloadJob?.cancel()

        downloadJob = lifecycleScope.launch {
            while (isActive) {
                val progress = withContext(Dispatchers.IO) {
                    getBytesAndStatus(requestID)
                }
                binding.icUpdateCheck.setContent("下载 ${progress}%")
                delay(1000)
            }
        }

        apkReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context, intent: Intent) {

                //已经完成
                if (intent.action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    binding.icUpdateCheck.setContent("下载 100%")

                    //解绑进度监听
                    downloadJob?.cancel()
                    downloadJob = null
                    //获取下载ID
                    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    val uri = downloadManager.getUriForDownloadedFile(id)

                    installApk(uri)

                } else if (intent.action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {

                    //如果还未完成下载，跳转到下载中心
                    val viewDownloadIntent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
                    viewDownloadIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(viewDownloadIntent)

                }

            }
        }

        registerReceiver(apkReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun installApk(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        startActivity(intent)
    }

    //获取当前进度，和总进度
    private fun getBytesAndStatus(downloadId: Long): Int {

        val query = DownloadManager.Query().setFilterById(downloadId)
        var cursor: Cursor? = null

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        try {
            cursor = downloadManager.query(query)
            if (cursor != null && cursor.moveToFirst()) {


                val downloaded = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                val total = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                val progress = downloaded * 100 / total
                Logger.w("当前下载大小：$downloaded 总共大小：$total, progress: ${progress}%")

                return progress
            }
        } finally {
            cursor?.close()
        }

        return 0
    }
}