package com.example.a220113_servicemessenger_2

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException
import javax.security.auth.callback.CallbackHandler
import android.os.Bundle


class RemoteService : Service() {
    private val TAG = "RemoteService"

    /*Socket.io 관련*/
    //클라이언트 소켓
    private var mSocket: Socket? = null
    private var isConnected = true

    companion object {
        val MSG_CLIENT_CONNECT = 1
        val MSG_CLIENT_DISCONNECT = 2
        val MSG_ADD_VALUE = 3
        val MSG_ADDED_VALUE = 4
        val MSG_SocketCon_VALUE = 5
        val BUNDLEKEY = "ActivityName"
    }

    //메신저 객체를 저장하는 해쉬맵
    // -Messenger객체를 저장
    // -해쉬맵에 저장된 Messenger를 통해 엑티비티에 응답을 보낼 수 있다.
    /* 결과값을 엑티비티에 전달 */
    private val mClientCallbacks = ArrayList<MessengerInfor>()
    private val mClientCallbacks_hash = HashMap<String, Messenger>()

    //Messenger에 콜백핸들러를 등록해서 Activity에서 오는 요청을 받을 수 있다.
    val mMessenger: Messenger = Messenger(CallbackHandler())
    var mValue = 0


    override fun onBind(intent: Intent): IBinder {
        //엑티비티가 서비스에 바인딩 될 때 전달해주는 객체
        //Ibiner로 Messenger객체를 만듬
        return mMessenger.binder;
    }

    override fun onCreate() {
        super.onCreate()

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        //소켓 연결 종료
        mSocket?.disconnect()
        mSocket?.off(Socket.EVENT_CONNECT, onConnect);
        mSocket?.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket?.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
    }

    //엑티비티가 보낸 이벤트를 처리하는 핸들러
    inner class CallbackHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                //서비스 엑티비티 연결
                MSG_CLIENT_CONNECT -> {
                    Log.e(TAG, "Received MSG_CLIENT_CONNECT message from client")
                    val bundle = msg.data
                    val activityName = bundle.getString(BUNDLEKEY)
                    //해쉬맵에 저장
                    //key: 엑티비티이름
                    //value: Messenger
                    mClientCallbacks_hash[activityName.toString()] = msg.replyTo
                    //소켓연결 요청
                    connect_Sokcet()
                }

                //서비스 엑티비티 끊기
                MSG_CLIENT_DISCONNECT -> {
                    Log.e(TAG, "Received MSG_CLIENT_DISCONNECT message from client")
                }

                //엑티비티가 MSG_ADD_VALUE이벤트를 전달할 때 실행되는 메서드
                MSG_ADD_VALUE -> {
                    Log.e(TAG, "Received message from client: MSG_ADD_VALUE")
                    mValue += msg.arg1
                    var i: Int = mClientCallbacks.size - 1
                    while (i >= 0) {
                        try {
                            Log.e(TAG, "클라이언트요청_Send MSG_ADDED_VALUE message to client")
                            val added_msg: Message = Message.obtain(
                                null, RemoteService.MSG_ADDED_VALUE
                            )
                            added_msg.arg1 = mValue
//                            mClientCallbacks.get(i).send(added_msg)
                        } catch (e: RemoteException) {
                            mClientCallbacks.removeAt(i)
                        }
                        i--
                    }
                }
            }
        }
    }


    private fun connect_Sokcet() {
        try {
            //클라이언트 소켓 생성
            mSocket = IO.socket("http://3.37.253.243:5000")
            //소켓 연결 관련 이벤트
            mSocket?.on(Socket.EVENT_CONNECT, onConnect);
            mSocket?.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket?.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            //서버와 소켓 연결 시작
            mSocket?.connect();

            //소켓이벤트 초기화
            initSocketEvent()

        } catch (e: URISyntaxException) {
            Log.e("MainActivity", e.reason)
        }
    }

    private val onConnect =
        Emitter.Listener { args: Array<Any?>? ->
            Log.e(TAG, "Socket.io_소켓 connected")

            //소켓연결 성공시 시작되는 코드
            try {
                Log.e(TAG, "Activity요청_Send MSG_ADDED_VALUE message to client")
                val added_msg: Message = Message.obtain(
                    null, MSG_SocketCon_VALUE
                )

                //엑티비티에 응답 보내기
                if (mClientCallbacks_hash.size != 0) {
                    Log.e(TAG, "mClientCallbacks_hash.size: " + mClientCallbacks_hash.size)
                    //1번인 경우 MainActivity에 응답 보내기
                    added_msg.arg1 = 10
//                    mClientCallbacks_hash["MainActivity"]?.send(added_msg)
                    mClientCallbacks_hash["SubActivity"]?.send(added_msg)
                }


            } catch (e: RemoteException) {
                mClientCallbacks.removeAt(0)
            }

        }

    private val onDisconnect =
        Emitter.Listener { args: Array<Any?>? ->
            Log.i(TAG, "Socket.io_소켓 diconnected")
            isConnected = false
        }

    private val onConnectError =
        Emitter.Listener { args: Array<Any?>? ->
            val s: Any? = args
        }


    //소켓 통신시 사용하는 이벤트 초기화
    private fun initSocketEvent() {
    }

}