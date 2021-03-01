package com.udacity

import android.app.Activity
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity

class DownloadNotificationManager(
    private val notificationChannelId: Int = R.string.notification_channel_id,
    private val notificationManager: NotificationManager
) {
    val notificationIcon = R.drawable.ic_assistant_black_24dp
    val notificationTitle = R.string.notification_title

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
        notificationId: Int,
        notificationClickIntent: PendingIntent?,
        messageBody: String,
        notifPriority: Int = NotificationCompat.PRIORITY_DEFAULT
    ) {
        val notification = context.configureNotification(
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
//        setLargeIcon(getLargeIconBitmap())
        priority = notifPriority
    }

    private fun Context.getLargeIconBitmap(): Bitmap =
        BitmapFactory.decodeResource(resources, notificationIcon)
}