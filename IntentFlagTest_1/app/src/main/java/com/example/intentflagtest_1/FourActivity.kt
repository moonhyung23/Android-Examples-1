package com.example.intentflagtest_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FourActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_four)
        val btnBack: Button = findViewById(R.id.btn_four_back)
        val btnComplete: Button = findViewById(R.id.btn_four_complete)

        btnComplete.setOnClickListener {
            multiFinish()
        }

        btnBack.setOnClickListener {
            finish()
        }

    }

    fun multiFinish() {
        finishAffinity()
    }
}