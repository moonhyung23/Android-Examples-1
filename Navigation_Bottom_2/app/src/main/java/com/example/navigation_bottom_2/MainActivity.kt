package com.example.navigation_bottom_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.navigation_bottom_2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //NavgationController 객체 생성
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container)
        val navController = navHostFragment?.findNavController()
        //바텀 메뉴에 Navgation 연결
        navController?.let { binding.bnMenu.setupWithNavController(it) }
    }
}