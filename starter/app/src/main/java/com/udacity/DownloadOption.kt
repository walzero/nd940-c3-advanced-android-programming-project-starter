package com.udacity

import android.net.Uri

enum class DownloadOption(val title: String, val uri: Uri) {
    GLIDE("Glide", Uri.parse(GLIDE_URL)),
    UDACITY("LoadApp", Uri.parse(UDACITY_URL)),
    RETROFIT("Retrofit", Uri.parse(RETROFIT_URL)),
    NONE("", Uri.EMPTY)
}

private const val GLIDE_URL = "https://github.com/bumptech/glide"
private const val UDACITY_URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
private const val RETROFIT_URL = "https://github.com/square/retrofit"