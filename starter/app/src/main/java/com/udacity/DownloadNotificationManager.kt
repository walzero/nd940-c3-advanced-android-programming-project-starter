package com.udacity

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import android.app.NotificationManager

class DownloadNotificationManager(
    private val notificationChannelId: Int = R.string.notification_channel_id,
    private val notificationManager: NotificationManager
) {
    val notificationIcon = R.drawable.ic_assistant_black_24dp
    val notificationTitle = R.string.notification_title

    fun Context.sendNotification(
        notificationId: Int,
        notificationClickIntent: PendingIntent?,
        messageBody: String,
        notifPriority: Int = NotificationCompat.PRIORITY_DEFAULT
    ) {
        val notification = configureNotification(
            notificationClickIntent,
            messageBody,
            notifPriority
        )

        notificationManager.notify(notificationId, notification.build())
    }


    private fun Context.configureNotification(
        notifClickIntent: PendingIntent?,
        messageBody: String,
        notifPriority: Int
    ) = NotificationCompat.Builder(this, getString(notificationChannelId)).apply {
        notifClickIntent?.let { setContentIntent(it) }
        setAutoCancel(true)
        setSmallIcon(notificationIcon)
        setContentTitle(getString(notificationTitle))
        setContentText(messageBody)
        setLargeIcon(getLargeIconBitmap())
        priority = notifPriority
    }

    private fun Context.getLargeIconBitmap(): Bitmap =
        BitmapFactory.decodeResource(resources, notificationIcon)
}