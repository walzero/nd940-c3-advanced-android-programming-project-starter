package com.udacity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.extensions.DOWNLOAD_TYPE
import com.udacity.extensions.cancelNotifications
import com.udacity.extensions.notificationManager

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setSupportActionBar(binding.toolbar)

        val type = intent.getSerializableExtra(DOWNLOAD_TYPE) as? DownloadOption
        Log.e("TESTING", type?.title ?: "test")

        if(intent.extras?.containsKey(DOWNLOAD_TYPE) == true) {
            notificationManager().cancelNotifications()
            title = type?.title
        }
    }

}
