package com.example.a211223_socket_io_chat

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.a211223_socket_io_chat.databinding.ActivityChatBinding
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class ChatActivity : AppCompatActivity() {
    //class name {
    val binding by lazy { ActivityChatBinding.inflate(layoutInflater) }

    //클라이언트 소켓
    private var mSocket: Socket? = null
    private var username: String? = null
    private var roomNumber: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()

    }

    private fun init() {
        try {
            //서버 ip
            mSocket = IO.socket("http://3.37.253.243:5000")
            Log.e("SOCKET", "Connection success : " + mSocket?.id())
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }


        val intent = intent
        username = intent.getStringExtra("username")
        roomNumber = intent.getStringExtra("roomNumber")
        mSocket?.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        //소켓 연결 종료
        mSocket?.disconnect()
    }
}