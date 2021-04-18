package com.udacity.extensions

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.DownloadOption
import com.udacity.R

// Notification ID.
private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0

const val DOWNLOAD_TYPE = "download_type"

fun NotificationManager.sendNotification(
    downloadOption: DownloadOption,
    applicationContext: Context,
    channelId: Int
) {
    val title = applicationContext.getString(R.string.notification_title)
    val body = applicationContext.getString(R.string.notification_description, downloadOption.title)
    val bigTextStyle = prepareBigTextStyle(title, body, applicationContext, downloadOption)
    val channelIdValue = applicationContext.getString(channelId)

    val detailIntent = Intent(applicationContext, DetailActivity::class.java)
    detailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    detailIntent.putExtra(DOWNLOAD_TYPE, downloadOption)

    val detailPendingIntent = PendingIntent.getActivity(
        applicationContext,
        REQUEST_CODE,
        detailIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    with(applicationContext) {
        val builder = NotificationCompat.Builder(
            this@with,
            getString(R.string.notification_channel_id)
        )
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(bigTextStyle)
            .setLargeIcon(createLargeIcon(applicationContext))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setChannelId(channelIdValue)
            .addAction(
                R.drawable.ic_baseline_arrow_circle_down_24,
                applicationContext.getString(R.string.notification_button),
                detailPendingIntent
            )

        notify(NOTIFICATION_ID, builder.build())
    }
}

private fun createLargeIcon(applicationContext: Context) =
    BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.ic_assistant_black_24dp
    )

private fun prepareBigTextStyle(
    title: String,
    message: String,
    applicationContext: Context,
    downloadOption: DownloadOption
) = NotificationCompat
    .BigTextStyle()
    .setBigContentTitle(title)
    .bigText(message)

fun NotificationManager.cancelNotifications() {
    cancelAll()
}

fun createChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            "Test Channel",
            context.getString(R.string.app_name),

            NotificationManager.IMPORTANCE_HIGH
        )
            .apply {
                setShowBadge(true)
            }

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = context.getString(R.string.app_description)

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}