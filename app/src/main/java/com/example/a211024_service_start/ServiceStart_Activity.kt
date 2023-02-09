package com.example.a211024_service_start

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.a211024_service_start.databinding.ActivityServiceStartBinding

class ServiceStart_Activity : AppCompatActivity() {
    //class name {
    val binding by lazy { ActivityServiceStartBinding.inflate(layoutInflater) }
    private var serviceIntent: Intent? = null

    /*서비스 관련 변수 및 객체*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //배터리 절전 모드 해제
        val pm = applicationContext.getSystemService(POWER_SERVICE) as PowerManager
        var isWhiteListing = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            isWhiteListing = pm.isIgnoringBatteryOptimizations(applicationContext.packageName)
        }
        if (!isWhiteListing) {
            val intent = Intent()
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:" + applicationContext.packageName)
            startActivity(intent)
        }

        //서비스 실행
        if (MyService.serviceIntent == null) {
            serviceIntent = Intent(this, MyService::class.java)
            startService(serviceIntent)
        } else {
            serviceIntent = MyService.serviceIntent //getInstance().getApplication();
            Toast.makeText(applicationContext, "already", Toast.LENGTH_LONG).show()
        }

    }

}