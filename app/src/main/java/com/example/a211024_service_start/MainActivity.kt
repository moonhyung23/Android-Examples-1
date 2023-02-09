package com.example.a211024_service_start

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.a211024_service_start.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //버튼클릭 리스너 등록
        binding.btn1.setOnClickListener(this)
    }




    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btn1.id -> {
                val intent = Intent(applicationContext, ServiceStart_Activity::class.java)
                startActivity(intent)
                finish()
            }
            else -> {
            }
        }
    }


}