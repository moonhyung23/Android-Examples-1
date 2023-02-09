package com.example.a211122_service_notifi

import android.R
import android.app.*
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.a211122_service_notifi.MyService.myServiceHandler

import android.app.NotificationManager
import android.content.Context
import android.content.Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT


class MyService : Service() {

    //노티피케이션
    var Notifi_M: NotificationManager? = null
    var thread: ServiceThread? = null

    companion object {
        var serviceIntent: Intent? = null
        const val NOTIFICATION_CHANNEL_ID = "10001"

        //0번 -> 실행
        //1번 -> 종료
        var stop_flag = 0
    }

    override fun onCreate() {
        Log.e("MyService", "onCreate")
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.e("MyService", "onBind")
        TODO("Not yet implemented")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Notifi_M = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val handler = myServiceHandler()
        thread = ServiceThread(handler)
        thread!!.start()
        Log.e("MyService", "onStartCommand")
        return START_NOT_STICKY

    }


    inner class myServiceHandler : Handler() {

        override fun handleMessage(msg: Message) {
            //노티피케이션 인텐트
            // -노티피케이션 클릭 시 엑티비티 이동
            // -전달할 데이터 설정
            var notificationIntent = Intent(this@MyService, MainActivity::class.java)
            notificationIntent.putExtra("notificationId", "1") //전달할 값
            notificationIntent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            //노피티케이션 인텐트 pending인텐트에 담기
            var pendingIntent = PendingIntent.getActivity(
                this@MyService,
                0,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            //노티피케이션 빌더
            val builder =
                NotificationCompat.Builder(this@MyService, NOTIFICATION_CHANNEL_ID)
                    .setTicker("알람 간단한 설명")
                    .setContentTitle("My notification")
                    .setContentText("Much longer text that cannot fit one line...")
                    // 더 많은 내용이라서 일부만 보여줘야 하는 경우 아래 주석을 제거하면 setContentText에 있는 문자열 대신 아래 문자열을 보여줌
//            .setStyle(NotificationCompat.BigTextStyle().bigText("더 많은 내용을 보여줘야 하는 경우..."))
                    .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                    // 사용자가 노티피케이션을 탭시 ResultActivity로 이동하도록 설정
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setOnlyAlertOnce(true)
                    .setAutoCancel(true)

            builder.setSmallIcon(R.drawable.alert_dark_frame) //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            val channelName: CharSequence = "노티페케이션 채널"
            val description = "오레오 이상을 위한 것임"
            val importance = NotificationManager.IMPORTANCE_HIGH
            //노티피케이션 채널에 입력할 것
            // -채널 ID, 채널이름, 중요도
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
            channel.description = description
            //노티피케이션 채널 생성
            Notifi_M?.createNotificationChannel(channel)
            Notifi_M?.notify(1234, builder.build()) // 고유숫자로 노티피케이션 동작시킴
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("MyService", "onDestroy")

    }
}