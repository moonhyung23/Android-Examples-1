package com.example.a220103_bindservice_1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Binder
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.*


class MyService : Service() {
    private val TAG = "MyService"


    companion object {
        var serviceIntent: Intent? = null


    }
    // 외부로 데이터를 전달하려면 바인더 사용

    // Binder 객체는 IBinder 인터페이스 상속구현 객체입니다
    //public class Binder extends Object implements IBinder

    // 외부로 데이터를 전달하려면 바인더 사용
    // Binder 객체는 IBinder 인터페이스 상속구현 객체입니다
    //public class Binder extends Object implements IBinder
    var iBinder: IBinder = MyBinder()

    var number = 1

    //서비스와 엑티비티가 통신하기 위해 필요함.
    inner class MyBinder : Binder() {
        fun getService(): MyService {
            // 액티비티와 서비스가 연결되면 이 메서드를 통해 서비스에 접근
            // 서비스 객체를 리턴
            return this@MyService
        }
    }

    //Service가 바인딩 되면 호출됨
    override fun onBind(intent: Intent?): IBinder {
        // 액티비티에서 bindService() 를 실행하면 호출됨
        // 리턴한 IBinder 객체는 서비스와 클라이언트 사이의 인터페이스 정의한다
        Log.e(TAG, "onBind")

        return iBinder // 서비스 객체를 리턴
    }

    fun getRan(): Int { // 임의 랜덤값을 리턴하는 메서드
        return Random().nextInt()
    }

    fun setNumber1(number: Int) { // 임의 랜덤값을 리턴하는 메서드
        this.number = number
    }


    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand")
        var th1: Unit = object : Thread("SocketConnect") {
            override fun run() {
                try {

                    while (true) {
                        Log.e(TAG, "Thread실행 중")

                        Thread.sleep(3000)
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }.start()


        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "onDestroy")

    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.e(TAG, "onTaskRemoved")

    }
}