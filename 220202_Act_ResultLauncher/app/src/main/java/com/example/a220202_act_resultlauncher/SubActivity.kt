package com.example.a220202_act_resultlauncher

import android.view.View


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a220202_act_resultlauncher.databinding.ActivitySubBinding

class SubActivity : AppCompatActivity(), View.OnClickListener {
    val binding by lazy { ActivitySubBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnSub.setOnClickListener(this)
    }


    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnSub.id -> {
                val intent = Intent(applicationContext, MainActivity::class.java).apply {
                    //엑티비티에서 갖고올 데이터
                    putExtra("KEY1", "bbbbb")
                    //데이터 전달이 성공했을 때의 변수 값 저장
                    // Result_ok = -1 일 때 엑티비티에 전달된다.

                }
                setResult(RESULT_OK, intent)
                //엑티비티 종료
                if (!isFinishing) finish()
            }
            else -> {
            }
        }
    }

}