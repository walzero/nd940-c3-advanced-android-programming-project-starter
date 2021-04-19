package com.udacity

import android.content.Intent
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

        if (intent.extras?.containsKey(DOWNLOAD_TYPE) == true) {
            notificationManager().cancelNotifications()
            title = type?.title
        }

        binding.contentDetail.btReturnHomeScreen.setOnClickListener { goToMainScreen() }
    }

    private fun goToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
    }

}
