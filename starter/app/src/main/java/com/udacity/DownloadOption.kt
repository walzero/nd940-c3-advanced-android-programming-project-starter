package com.udacity

import android.net.Uri

enum class DownloadOption(val uri: Uri) {
    GLIDE(Uri.parse(GLIDE_URL)),
    UDACITY(Uri.parse(UDACITY_URL)),
    RETROFIT(Uri.parse(RETROFIT_URL))
}

private const val GLIDE_URL = "https://github.com/bumptech/glide"
private const val UDACITY_URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
private const val RETROFIT_URL = "https://github.com/square/retrofit"