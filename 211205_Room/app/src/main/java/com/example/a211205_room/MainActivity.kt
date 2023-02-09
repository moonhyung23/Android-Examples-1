package com.example.a211205_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amitshekhar.DebugDB

class MainActivity : AppCompatActivity() {
    val handler: Handler = Handler()
    var r1: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = AppDatabase.getInstance(applicationContext)


        //스레드 선언하는 부분
        r1 = db?.let { db_Runnable(handler, it) }
        var th1: Thread = Thread(r1)
        th1.start()

        val mHandler: Handler = Handler {
            if (it.what == 0) {
                Toast.makeText(applicationContext, "저장완료", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("RealService_OnDestroy", "종료")
    }


    class db_Runnable : Runnable {
        var handler: Handler? = null
        var flag = true
        var db: AppDatabase? = null

        constructor(handler: Handler, db: AppDatabase) {
            this.handler = handler
            this.db = db
        }

        override fun run() {

            while (true) {
                if (flag) {
                    flag = false
                    db?.userDao()?.insertAll(User("1", "2"))
                    handler?.sendEmptyMessage(0)
                }
            }
        }
    }


}