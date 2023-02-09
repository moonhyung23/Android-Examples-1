package com.example.coroutinetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val job = GlobalScope.launch(Dispatchers.Default) {
            Log.e(TAG, "Starting long running calculation")
            for (i in 30..40) {
                Log.e(TAG, "Result for i = $i : ${fib(i)}")

            }
            Log.e(TAG, "Ending Long running calculation")

        }

        runBlocking {

            delay(2000L)
            job.cancel()
            Log.e(TAG, "job canceled")
        }

    }

    fun fib(n: Int): Long {
        return if (n == 0) 0
        else if (n == 1) 1
        else fib(n - 1) + fib(n - 2)
    }
}