package com.example.a220103_bindservice_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import android.content.Intent


import android.content.ComponentName
import android.content.Context

import com.example.a220103_bindservice_1.MyService.MyBinder

import android.os.IBinder

import android.content.ServiceConnection
import android.view.View
import android.widget.Button


class MainActivity : AppCompatActivity() {
    private var serviceIntent: Intent? = null

    var ms // 서비스 객체
            : MyService? = null
    var isService = false // 서비스 중인 확인용
    var conn: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            name: ComponentName,
            service: IBinder,
        ) {
            // 서비스와 연결되었을 때 호출되는 메서드
            // 서비스 객체를 전역변수로 저장
            val mb = service as MyBinder
            ms = mb.getService() // 서비스가 제공하는 메소드 호출하여
            // 서비스쪽 객체를 전달받을수 있슴
            isService = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
// 서비스와 연결이 끊겼을 때 호출되는 메서드
            isService = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 데이터를 전달할 수 있는 서비스 사용하기
// 1. 다음 Service (*.java)를 작성한다
// 2. Service 를 등록한다 AndroidManifest.xml
// 3. Service 를 시작한다


        val b1: Button = findViewById<View>(R.id.button1) as Button
        val b2: Button = findViewById<View>(R.id.button2) as Button
        val b3: Button = findViewById<View>(R.id.button3) as Button
        val b4: Button = findViewById<View>(R.id.button4) as Button
        //서비스 바인딩
        val intent = Intent(
            this@MainActivity,  // 현재 화면
            MyService::class.java) // 다음넘어갈 컴퍼넌트
        bindService(intent,  // intent 객체
            conn,  // 서비스와 연결에 대한 정의
            0)

        //서비스 시작
        b1.setOnClickListener {
            //서비스가 실행되어있지 않은 경우에만
            if (MyService.serviceIntent == null) {
                MyService.serviceIntent = Intent(this, MyService::class.java)
                startService(MyService.serviceIntent)
            }
            //이미 실행된 경우
            else {
                Toast.makeText(applicationContext, "already", Toast.LENGTH_LONG).show()
            }
        }

        //서비스 종료
        b2.setOnClickListener {
            unbindService(conn) // 서비스 종료
        }


        //데이터 확인하기
        b3.setOnClickListener {
            //서비스가 실행중이지 않은 경우
            if (!isService) {
                Toast.makeText(applicationContext,
                    "서비스중이 아닙니다, 데이터받을수 없음",
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            //실행 중인 경우
            //서비스객체를 이용해 서비스안에 있는 메서드를 실행
            val num = ms!!.getRan() //서비스쪽 메소드로 값 전달 받아 호출
            Toast.makeText(applicationContext,
                "받아온 데이터 : $num",
                Toast.LENGTH_LONG).show()

        }
        //서비스 종료
        b4.setOnClickListener {
            ms?.setNumber1(2)
            val intent = Intent(applicationContext, SubActivity::class.java)
            startActivity(intent)
            finish()
        }


    } // end of onCreate


} // end of class


