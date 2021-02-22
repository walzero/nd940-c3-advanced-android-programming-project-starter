package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        binding.contentMain.customButton.setOnClickListener {
//            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download(downloadOption: DownloadOption) {
        val request =
            DownloadManager.Request(downloadOption.uri)
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

//    private val PROGRESS_DELAY = 1000L
//    var handler: Handler = Handler()
//    private var isProgressCheckerRunning = false
//
//    private fun stopProgressChecker() {
//        handler.removeCallbacks(progressChecker)
//        isProgressCheckerRunning = false
//    }
//
//    private fun startProgressChecker() {
//        if (!isProgressCheckerRunning) {
//            progressChecker.run()
//            isProgressCheckerRunning = true
//        }
//    }
//
//    private val progressChecker: Runnable = object : Runnable {
//        override fun run() {
//            try {
//                checkProgress()
//                // manager reference not found. Commenting the code for compilation
//                //manager.refresh();
//            } finally {
//                handler.postDelayed(this, PROGRESS_DELAY)
//            }
//        }
//    }
//
//    private fun checkProgress() {
//        val query = DownloadManager.Query()
//        query.setFilterByStatus((DownloadManager.STATUS_FAILED or DownloadManager.STATUS_SUCCESSFUL).inv())
//        val cursor: Cursor = downloadManager.query(query)
//        if (!cursor.moveToFirst()) {
//            cursor.close()
//            return
//        }
//        do {
//            val reference: Long = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID))
//            val progress: Long =
//                cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
//            // do whatever you need with the progress
//        } while (cursor.moveToNext())
//        cursor.close()
//    }

    companion object {
        private const val CHANNEL_ID = "channelId"
    }

}
