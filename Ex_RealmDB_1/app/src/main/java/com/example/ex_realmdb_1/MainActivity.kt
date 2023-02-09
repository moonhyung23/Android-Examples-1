package com.example.ex_realmdb_1

import android.view.View


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ex_realmdb_1.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.kotlin.where

class MainActivity : AppCompatActivity(), View.OnClickListener {
    //class name {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.button.setOnClickListener(this)
        binding.button2.setOnClickListener(this)
        binding.button3.setOnClickListener(this)
        binding.button4.setOnClickListener(this)
        binding.button5.setOnClickListener(this)
        realm = Realm.getDefaultInstance()
    }


    //버튼클릭 리스너 등록

    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1) 저장
            binding.button.id -> {
                val user = User(1, "moon", 26)
                App.instance.insert(user)


            }

            //2) 수정
            binding.button2.id -> {
                val id = 1
                App.realm.beginTransaction()
                val user = App.realm.where<User>().equalTo("id", id).findFirst()
                user!!.name = "moon2"
                user.age = 27
                realm.commitTransaction()
            }

            //3) 삭제
            binding.button3.id -> {

            }

            //4)조회
            binding.button4.id -> {
                Toast.makeText(applicationContext,
                    App.instance.getUser(1).toString(),
                    Toast.LENGTH_SHORT)
                    .show()
            }

            binding.button5.id -> {

            }
            else -> {
            }
        }
    }


}