package com.example.a211031_ex_handler_2_class

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.TextView

/*
작업스레드가 메인스레드와 완전히 분리되어 있어서 메인스레드에서 생성한 핸들러를 작업스레드에서
직접 참조 할수 없을때, Message 생성자 함수로 메세지를 생성하여 보내주면 됩니다.
*/
class MainActivity : AppCompatActivity() {
    var mainValue = 0
    var backValue = 0
    var mainText: TextView? = null
    var backText: TextView? = null
    lateinit var backthread: BackThread

    var handler: Handler = Handler();
    var thread_stopFlag: Int = 1

    companion object {
        var addflag = 0;
        var infor1 = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainText = findViewById<View>(R.id.mainvalue) as TextView
        backText = findViewById<View>(R.id.backvalue) as TextView


        // 메인스레드의 핸들러 객체를 외부 클래스에 넘겨줌
        backthread = BackThread(handler)
        val thread = Thread(backthread)
        thread.isDaemon = true
        thread.start()

        object : Thread() {
            override fun run() {
                super.run()
                while (thread_stopFlag == 1) {
                    if (addflag == 1) {
                        handler.post {
                            Log.e("!", "핸들러작동")
//                            mainText!!.text = infor1
                            mainText!!.text = backthread.backValue.toString()
                        }
                    }
                }
            }
        }.start()


    } // end onCreate()

    fun mOnClick(v: View?) {
        addflag = 1
    }
} // end class


//  메인스레드의 핸들러를 직접 사용할수 없는 분리된 작업 스레드
class BackThread     // end constructor
    (var handler: Handler) : Runnable {
    var backValue = 0

    override fun run() {
        while (true) {

            if (MainActivity.addflag == 1) {
//                MainActivity.infor1 = "하이"
                backValue++
                MainActivity.addflag = 0
            }
        }
    } // end run()
} // end class

