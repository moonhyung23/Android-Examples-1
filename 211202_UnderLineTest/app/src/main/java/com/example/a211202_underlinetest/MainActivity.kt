package com.example.a211202_underlinetest

import android.app.Activity
import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.TextView
import com.example.a211202_underlinetest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        
        val content = SpannableString(binding.tv1.text.toString())
        content.setSpan(UnderlineSpan(), 0, 3, 0)
        binding.tv1.text = content

    }
}