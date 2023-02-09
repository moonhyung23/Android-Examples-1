package com.example.a211121_notification_push

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.R
import android.app.*

import android.os.Build

import androidx.core.app.NotificationCompat

import android.graphics.BitmapFactory

import android.content.Context

import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.core.app.NotificationManagerCompat
import com.example.a211121_notification_push.databinding.ActivityMainBinding
import android.media.RingtoneManager
import android.net.Uri


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var count = 0
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.button.setOnClickListener(this)

    }

    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1) 버튼1
            binding.button.id -> {
                // 버튼을 누를때마다 count 를 증가시며 최근에 보낸 노티피케이션만 사용자의 탭 대기중인지 테스트
                count++
                NotificationSomethings()
            }
            else -> {
            }
        }
    }

    fun NotificationSomethings() {
        //노티피케이션 메니저 객체 생성
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //노티피케이션 인텐트
        // -노티피케이션 클릭 시 엑티비티 이동
        // -전달할 데이터 설정
        val notificationIntent = Intent(this, result_main::class.java)
        notificationIntent.putExtra("notificationId", count) //전달할 값
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        //노피티케이션 인텐트 pending인텐트에 담기
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        //노티피케이션 빌더
        val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setTicker("알람 간단한 설명")
            .setContentTitle("My notification")
            .setContentText("Much longer text that cannot fit one line...")
            // 더 많은 내용이라서 일부만 보여줘야 하는 경우 아래 주석을 제거하면 setContentText에 있는 문자열 대신 아래 문자열을 보여줌
//            .setStyle(NotificationCompat.BigTextStyle().bigText("더 많은 내용을 보여줘야 하는 경우..."))
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)

        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.alert_dark_frame) //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            builder.setLargeIcon(
                BitmapFactory.decodeResource(
                    getResources(),
                    R.drawable.ic_dialog_alert
                )
            )
            builder.setDefaults(NotificationCompat.DEFAULT_SOUND)
            val channelName: CharSequence = "노티페케이션 채널"
            val description = "오레오 이상을 위한 것임"
            val importance = NotificationManager.IMPORTANCE_HIGH
            //노티피케이션 채널에 입력할 것
            // -채널 ID, 채널이름, 중요도
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
            channel.description = description
            //노티피케이션 채널 생성
            notificationManager.createNotificationChannel(channel)
        } else {
            // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
            builder.setSmallIcon(R.mipmap.sym_def_app_icon)
            builder.setDefaults(NotificationCompat.DEFAULT_SOUND)
        }
        notificationManager.notify(1234, builder.build()) // 고유숫자로 노티피케이션 동작시킴
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "10001"
    }
}