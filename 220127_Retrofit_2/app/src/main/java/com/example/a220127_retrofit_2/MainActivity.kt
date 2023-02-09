package com.example.a220127_retrofit_2

import androidx.databinding.DataBindingUtil


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.a220127_retrofit_2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"

    val api by lazy { APIS.create() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //post로 보내기버튼 리스너
        binding.postbutton.setOnClickListener(this)
    }

    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //포스트로 보내기 버튼
            binding.postbutton.id -> {

                api.post_users(
                    binding.idedt.text.toString(), // EditText1
                    binding.nickedt.text.toString(), //EditText2
                    binding.pwdedt.text.toString(), //EditText3
                ).enqueue(object : Callback<PostModel> {
                    //서버 요청 성공
                    override fun onResponse(
                        call: Call<PostModel>,
                        response: Response<PostModel>,
                    ) {
                        Log.d(TAG, response.toString())
                        Log.d(TAG, response.body().toString())
                        if (!response.body().toString().isEmpty())
                            binding.text.text = response.body().toString();
                    }

                    //서버 요청 실패 ㅠ
                    override fun onFailure(call: Call<PostModel>, t: Throwable) {
                        Log.d(TAG, t.message.toString())
                    }
                })
            }
            else -> {
            }
        }
    }

}

//Post방식으로 서버에 전달할 데이터를 파라미터에 입력
