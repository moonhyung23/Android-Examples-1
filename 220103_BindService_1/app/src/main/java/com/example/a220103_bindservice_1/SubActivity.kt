package com.example.a220103_bindservice_1

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log

class SubActivity : AppCompatActivity() {
    // 서비스 객체
    var ms: MyService? = null
    var isService = false // 서비스 중인 확인용
    val TAG = "SubActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        val intent = Intent(
            applicationContext,  // 현재 화면
            MyService::class.java) // 다음넘어갈 컴퍼넌트
        bindService(intent,  // intent 객체
            conn,  // 서비스와 연결에 대한 정의
            0)


    }


    var conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName,
            service: IBinder,
        ) {
            // 서비스와 연결되었을 때 호출되는 메서드
            // 서비스 객체를 전역변수로 저장
            val mb = service as MyService.MyBinder
            ms = mb.getService() // 서비스가 제공하는 메소드 호출하여
            // 서비스쪽 객체를 전달받을수 있슴
            isService = true
            Log.e(TAG, "number: ${ms?.number}")
        }

        override fun onServiceDisconnected(name: ComponentName) {
// 서비스와 연결이 끊겼을 때 호출되는 메서드
            isService = false
        }
    }
}