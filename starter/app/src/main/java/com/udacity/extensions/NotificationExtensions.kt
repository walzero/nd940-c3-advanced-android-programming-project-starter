package com.udacity.extensions

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.udacity.R

fun NotificationManager.sendNotification(
    messageBody: String,
    applicationContext: Context,
    pendingIntent: PendingIntent,
    notificationId: Int
) {

    val notificationIcon = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.ic_assistant_black_24dp
    )

    with(applicationContext) {
        val builder = NotificationCompat.Builder(
            this@with,
            getString(R.string.notification_channel_id)
        )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_assistant_black_24dp)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(messageBody)
            .setLargeIcon(notificationIcon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .addAction(
//                R.drawable.egg_icon,
//                applicationContext.getString(R.string.snooze),
//                snoozePendingIntent
//            )

        notify(notificationId, builder.build())

    }
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}