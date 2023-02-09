package com.example.a211223_socket_io_chat

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a211223_socket_io_chat.databinding.ActivityMainBinding
import android.content.Intent
import android.util.Log
import java.net.URISyntaxException
import io.socket.client.IO;
import io.socket.client.Socket;
/* 소켓 연결 예제 1 */
class MainActivity : AppCompatActivity() {
    //class name {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initUI();
    }

    private fun initUI() {
        binding.btnJoin.setOnClickListener { v ->
            val intent = Intent(applicationContext, ChatActivity::class.java)
            //입력한 내용
            intent.putExtra("username", binding.edUserName.text.toString())
            intent.putExtra("roomNumber", binding.edRoomNumber.text.toString())
            startActivity(intent)
        }
    }


}