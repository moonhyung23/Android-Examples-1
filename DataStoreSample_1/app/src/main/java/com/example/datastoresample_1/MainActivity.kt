package com.example.datastoresample_1

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.datastoresample_1.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        /*   //코루틴을 사용해 Retrofit API 통신을 진행
           runBlocking {
               //결과값을 반환하기 위해 Deffered 사용
               val response = async {
                   App.instance.saveInt("example_counter", 3)
               }
               response.await()
           }*/

        lifecycleScope.launch {

            val response = async {
                App.instance.saveInt("example_counter", 6)
            }
            response.await()
        }

        //코루틴을 사용해 Retrofit API 통신을 진행
        runBlocking {
            //결과값을 반환하기 위해 Deffered 사용
            val response = async {
                App.instance.readInt("example_counter")
            }

            println("반환값2: ${response.await()}")
        }


    }


}