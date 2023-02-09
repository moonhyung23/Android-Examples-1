package com.example.intentflagtest_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        val btnNext: Button = findViewById(R.id.btn_third_next)
        val btnback: Button = findViewById(R.id.btn_third_back)
        //다음
        btnNext.setOnClickListener {
            val intent = Intent(applicationContext, FourActivity::class.java)
            startActivity(intent)
        }
        //뒤로
        btnback.setOnClickListener {

            finish()
        }
    }
}