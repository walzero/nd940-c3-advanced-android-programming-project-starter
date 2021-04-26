package com.udacity

import android.net.Uri
import androidx.annotation.StringRes

enum class DownloadOption(val title: String, @StringRes val description: Int, val uri: Uri) {
    GLIDE("Glide", R.string.download_option_glide, Uri.parse(GLIDE_URL)),
    UDACITY("LoadApp", R.string.download_option_udacity, Uri.parse(UDACITY_URL)),
    RETROFIT("Retrofit", R.string.download_option_retrofit, Uri.parse(RETROFIT_URL)),
    NONE("", -1, Uri.EMPTY)
}

private const val GLIDE_URL = "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
private const val UDACITY_URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/master.zip"
private const val RETROFIT_URL = "https://github.com/square/retrofit/archive/refs/heads/master.zip"