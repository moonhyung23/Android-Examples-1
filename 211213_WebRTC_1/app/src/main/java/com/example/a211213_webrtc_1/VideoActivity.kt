package com.example.a211213_webrtc_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.a211213_webrtc_1.databinding.ActivityVideoBinding
import com.remotemonster.sdk.RemonCall

class VideoActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoBinding
    var remonCall: RemonCall? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video)
        //영상통화 하는동안 화면이 꺼지지 않게 하기
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        remonCall = RemonCall.builder()
            .context(this)
            .serviceId("SERVICEID1")
            .key("12345678")
            .videoCodec("VP8")
            .videoWidth(648)
            .videoHeight(480)
            .localView(binding.localView)
            .remoteView(binding.remoteView)
            .build()

        val channelId = intent.getStringExtra("channelId")
        remonCall?.connect(channelId)
        /* //상대방이 종료했을 경우
         remonCall?.onClose {
             finish()
         }*/
    }

    override fun onDestroy() {
        remonCall?.close()
        super.onDestroy()


    }
}