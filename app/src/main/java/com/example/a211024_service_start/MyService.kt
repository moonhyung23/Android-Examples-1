package com.example.a211024_service_start

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class MyService : Service() {
    var aws_ip = "3.37.253.243"
    private var context: Context? = null
    var socket: Socket? = null


    var port = 7777
    var sendwriter: PrintWriter? = null
    private var th_send: Thread? = null
    private var th_write: Thread? = null


    companion object {
        var serviceIntent: Intent? = null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        //서버에서 온 메세지를 클라이언트에서 읽는 쓰레드(수신)
        if (th_write == null) {
            th_write = object : Thread("WriteThread") {
                override fun run() {
                    try {
                        /* 소켓 통신에 필요한 객체 초기화*/
                        //소켓 서버 접속
                        socket = Socket(aws_ip, port)
                        println("서버에 접속 성공!")

                        // * 1.InputStream
                        //InputStream: 서버에서 보낸 메세지를 읽을 때 사용
                        //socket의 InputStream 정보를 InputStream 객체에 넣는다.
                        val input = socket!!.getInputStream()
                        // InputStream에 있는 내용(서버에서 보낸 메세지)를 읽기 위해
                        // BufferedReader객체에 넣는다.
                        val reader = BufferedReader(InputStreamReader(input))

                        // * 2.OutputStream
                        //OutputStream: 클라이언트 -> 서버로 메세지 전송 할 때 사용
                        // socket에 outputStream을 연결
                        val out = socket!!.getOutputStream()
                        //PrintWriter를 사용해 OutputStream에 있는 내용을 서버에 보낸다.
                        sendwriter = PrintWriter(out, true)


                        /* 서버에서 오는 메세지를 기다린다. */
                        while (!currentThread().isInterrupted) {
//                            System.out.println("서버응답 기다리는 중 ");
                            SingleTon.API.readMsg_content = reader.readLine()
                            Log.e("서버응답  ", SingleTon.API.readMsg_content)
                            SingleTon.API.add_Flag = 1
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            //메세지를 읽는 쓰레드 시작
            th_write?.start()
        }


        //서버에 메세지를 보내는 쓰레드(송신)
        if (th_send == null) {
            th_send = object : Thread("SendThread") {
                override fun run() {
                    while (!currentThread().isInterrupted) {
                        //sendMsg_flag
                        // 1번 -> 엑티비티에서 채팅 보내기 버튼 클릭
                        //실행되는 코드
                        if (SingleTon.API.sendMsg_flag == 1) {
                            //서버에 보낼 메세지
                            sendwriter?.println(SingleTon.API.SendMsg_split)
                            sendwriter!!.flush()
                            Log.e("보낸메세지", SingleTon.API.SendMsg_split)
                            SingleTon.API.sendMsg_flag = 0
                        }
                    }
                }
            }
            //메세지를 보내는 스레드 시작
            th_send?.start()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("RealService_OnDestroy", "종료")
        //쓰레드가 살아있는 경우 종료
        serviceIntent = null
        //1)서버에 메세지를 보내는 쓰레드 종료
        if (th_send != null && th_send!!.isAlive()) {
            th_send!!.interrupt()
            th_send = null
        }
        //2)메세지를 읽는 쓰레드 종료
        if (th_write != null && th_write!!.isAlive()) {
            th_write!!.interrupt()
            th_write = null
        }
        stopSelf()
    }
}