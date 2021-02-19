package com.udacity.extensions

import android.app.NotificationManager
import android.content.Context

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}