package com.example.a211122_service_notifi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.a211122_service_notifi.databinding.ActivityMainBinding
import com.example.a211122_service_notifi.databinding.ActivitySubBinding

class SubActivity : AppCompatActivity(), View.OnClickListener {
    val binding by lazy { ActivitySubBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnMove.setOnClickListener(this)
        Log.e("Sub", "onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.e("Sub", "onStart")

    }

    override fun onResume() {
        super.onResume()
        Log.e("Sub", "onResume")
    }

    override fun onStop() {
        super.onStop()
        Log.e("Sub", "onStop")
    }

    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1) 버튼1
            binding.btnMove.id -> {
                Log.e("btnMove", "버튼1")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }

            else -> {
            }
        }
    }

    override fun onDestroy() {
        Log.e("Sub", "ondestroy")
        super.onDestroy()
    }
}