package com.udacity

import android.Manifest
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.udacity.databinding.ActivityMainBinding
import com.udacity.extensions.DOWNLOAD_TYPE
import com.udacity.extensions.createChannel
import com.udacity.extensions.showShortToast
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: DownloadSourceViewModel

    private val storagePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE

    private var requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                startDownload(viewModel.downloadOption)
                return@registerForActivityResult
            }

            showPermissionRequiredSnackBar()
        }

    private val downloadNotifManager: DownloadNotificationManager by lazy {
        DownloadNotificationManager(notificationManager = notificationManager)
    }

    private var downloadID: Long = 0

    private val notificationManager: NotificationManager by lazy {
        getSystemService(NotificationManager::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(DownloadSourceViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.contentMain.viewModel = viewModel

        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        downloadNotifManager.createChannel(
            this,
            getString(R.string.notification_channel_name)
        )

        setListeners()
        setObservers()
        createChannel(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        if (!hasWritePermission()) {
            requestPermission.launch(storagePermission)
        }
    }

    private fun setObservers() {
        viewModel.hasChosenDownloadOption.observe(this, {
            binding.contentMain.customButton.setButtonActiveState(it ?: false)
        })
    }

    private fun setListeners() {
        binding.contentMain.run {
            rgDownloadOptions.setOnCheckedChangeListener { _, checkedId ->
                onCheckedItem(checkedId)
            }

            customButton.setOnClickListener { startDownload(viewModel?.downloadOption) }
        }
    }

    private fun onCheckedItem(checkedId: Int) {
        val option = when (checkedId) {
            R.id.rb_option_glide -> DownloadOption.GLIDE
            R.id.rb_option_udacity -> DownloadOption.UDACITY
            R.id.rb_option_retrofit -> DownloadOption.RETROFIT
            else -> DownloadOption.NONE
        }

        viewModel.setDownloadOption(option)
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
        showShortToast("Download do projeto ${viewModel.downloadOption.title} finalizado")
        showNotification()
    }

    private fun showNotification() {
        downloadNotifManager.sendNotification(
            this,
            viewModel.downloadOption
        )
    }

    private fun startDownload(downloadOption: DownloadOption?) {
        if (!hasWritePermission()) {
            requestPermission.launch(storagePermission)
            return
        }

        download(downloadOption)
    }

    private fun download(downloadOption: DownloadOption?) {
        val fileName = "${downloadOption?.title}.zip"

        val request =
            DownloadManager.Request(downloadOption?.uri)
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
//                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun hasWritePermission() =
        ContextCompat.checkSelfPermission(
            this,
            storagePermission
        ) == PackageManager.PERMISSION_GRANTED

    private fun showPermissionRequiredSnackBar() {
        val snackbar = Snackbar.make(
            binding.root,
            R.string.permission_denied_explanation,
            Snackbar.LENGTH_SHORT
        )

        val view: View = snackbar.view
        val params = view.layoutParams as CoordinatorLayout.LayoutParams
        params.gravity = Gravity.TOP
        view.layoutParams = params

        snackbar.show()
    }
}
