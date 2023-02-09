package com.example.ex_fragmentresult_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var btn: Button = findViewById(R.id.button)
        btn.setOnClickListener {
            replaceFragment(frag_1(), "mypage")
        }
    }

    fun replaceFragment(fragment: Fragment, fragmentTag: String) {
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.replace(, fragment, fragmentTag).commit()
    }
}