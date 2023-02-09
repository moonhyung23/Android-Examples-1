package com.example.intentflagtest_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val btnNext: Button = findViewById(R.id.btn_second_next)
        val btnback: Button = findViewById(R.id.btn_second_back)
        //다음
        btnNext.setOnClickListener {

            val intent = Intent(applicationContext, ThirdActivity::class.java)
            //NEW TASK를 추가하지 않으면 기존 TASK와 같이 관린된다.
            startActivity(intent)
        }

        //뒤로
        btnback.setOnClickListener {
            finishAffinity()
        }
    }
}