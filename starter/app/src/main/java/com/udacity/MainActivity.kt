package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityMainBinding
import com.udacity.extensions.showShortToast
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val downloadNotifManager: DownloadNotificationManager by lazy {
        DownloadNotificationManager(notificationManager = notificationManager)
    }

    private val contentIntent by lazy {
        Intent(applicationContext, MainActivity::class.java)
    }

    private var downloadID: Long = 0

    private var downloadOption = DownloadOption.NONE

    private val notificationManager: NotificationManager by lazy {
        getSystemService(NotificationManager::class.java)
    }
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        downloadNotifManager.createChannel(
            this,
            getString(R.string.notification_channel_name)
        )

        configureViews()
        setListeners()
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            Timber.d("DownloadId: $id")

            if (downloadID == id)
                notifyUser()
        }
    }

    private fun notifyUser() {
        showShortToast("Download do projeto ${downloadOption.title} finalizado")
        showNotification()
    }

    private fun showNotification() {
        pendingIntent = createPendingIntent()

        downloadNotifManager.sendNotification(
            context = this,
            notificationId = downloadID.toInt(),
            pendingIntent,
            getString(R.string.notification_description, downloadOption.title)
        )
    }

    private fun createPendingIntent() = PendingIntent.getActivity(
        applicationContext,
        downloadID.toInt(),
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    private fun configureViews() {
        with(binding) {
            //animate view if has selected a value
            contentMain.customButton.validateClick = {
                hasDownloadOption()
            }
        }
    }

    private fun setListeners() {
        setDownloadButtonClickListener()
        setCheckedChangedListener()
    }

    private fun setDownloadButtonClickListener() {
        binding.contentMain.customButton.setOnClickListener {
            when (hasDownloadOption()) {
                true -> {
                    download(downloadOption)
                    return@setOnClickListener
                }
                false -> showShortToast(getString(R.string.choose_an_option))
            }
        }
    }

    private fun setCheckedChangedListener() {
        with(binding.contentMain) {
            rgDownloadOptions.setOnCheckedChangeListener { _, checkedId ->
                downloadOption = when (checkedId) {
                    rbOptionGlide.id -> DownloadOption.GLIDE
                    rbOptionUdacity.id -> DownloadOption.UDACITY
                    rbOptionRetrofit.id -> DownloadOption.RETROFIT
                    else -> DownloadOption.NONE
                }
            }
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

    private fun hasDownloadOption() = downloadOption != DownloadOption.NONE
}
