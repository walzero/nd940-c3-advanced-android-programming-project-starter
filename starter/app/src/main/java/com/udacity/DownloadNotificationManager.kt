package com.udacity

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import com.udacity.extensions.sendNotification

class DownloadNotificationManager(
    private val notificationChannelId: Int = R.string.notification_channel_id,
    private val notificationManager: NotificationManager
) {

    fun createChannel(activity: Activity, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                activity.getString(notificationChannelId),
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description =
                activity.getString(R.string.notification_channel_id)

            val notificationManager = activity.getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun sendNotification(
        context: Context,
        downloadOption: DownloadOption
    ) {
        notificationManager.sendNotification(
            applicationContext = context,
            downloadOption = downloadOption,
            channelId = notificationChannelId
        )
    }
}