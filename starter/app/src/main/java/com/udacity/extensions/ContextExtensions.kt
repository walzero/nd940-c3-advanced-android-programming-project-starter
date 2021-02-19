package com.udacity.extensions

import android.app.NotificationManager
import android.content.Context
import android.widget.Toast
import androidx.core.content.ContextCompat

fun Context.notificationManager(): NotificationManager {
    return ContextCompat.getSystemService(
        this,
        NotificationManager::class.java
    ) as NotificationManager
}

fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}