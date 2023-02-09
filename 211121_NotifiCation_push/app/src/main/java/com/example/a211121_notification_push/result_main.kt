package com.example.a211121_notification_push

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.NotificationManager

import android.R
import android.content.Context
import android.view.View

import android.widget.TextView
import com.example.a211121_notification_push.databinding.ActivityMainBinding
import com.example.a211121_notification_push.databinding.ActivityResultMainBinding


class result_main : AppCompatActivity() {
    val binding by lazy { ActivityResultMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var text = "전달 받은 값은"
        var id = 0
        val extras = intent.extras
        if (extras == null) {
            text = "값을 전달 받는데 문제 발생"
        } else id = extras.getInt("notificationId")
        binding.textView.text = "$text $id"
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //노티피케이션 제거
        notificationManager.cancel(id)
    }
}