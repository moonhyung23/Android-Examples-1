package com.example.a211122_service_notifi

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.a211122_service_notifi.databinding.ActivityMainBinding
import android.content.Intent
import android.content.IntentSender
import android.util.Log

import android.widget.Toast


class MainActivity : AppCompatActivity(), View.OnClickListener {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("MainActivity", "onCreate")
        setContentView(binding.root)


        //버튼클릭 리스너 등록
        binding.btn1.setOnClickListener(this)
        binding.btn2.setOnClickListener(this)
        binding.btn3.setOnClickListener(this)
    }


    override fun startIntentSender(
        intent: IntentSender?,
        fillInIntent: Intent?,
        flagsMask: Int,
        flagsValues: Int,
        extraFlags: Int,
        options: Bundle?
    ) {
        super.startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, options)
    }

    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1) 버튼1
            binding.btn1.id -> {
                Log.e("onCreate", "버튼1")
                Toast.makeText(applicationContext, "Service 시작", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, MyService::class.java)
                startService(intent)

            }
            //2) 버튼2
            binding.btn2.id -> {
                Log.e("onCreate", "버튼2")
                Toast.makeText(applicationContext, "Service 끝", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, MyService::class.java)
                stopService(intent)
            }

            //3) 버튼3
            binding.btn3.id -> {
                Log.e("onCreate", "버튼3")
                Toast.makeText(applicationContext, "Service 끝", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, SubActivity::class.java)
                startActivity(intent)
                finish()
            }
            else -> {
            }
        }

    }

    override fun onStart() {
        super.onStart()
        Log.e("MainActivity", "onStart")

    }

    override fun onResume() {
        super.onResume()
        Log.e("MainActivity", "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.e("MainActivity", "onStop")
    }

    override fun onDestroy() {

        Log.e("MainActivity", "ondestroy")
        super.onDestroy()
    }
}