package com.example.a210112_servicemessenger

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import javax.security.auth.callback.CallbackHandler


class RemoteService : Service() {
    private val TAG = "RemoteService"

    companion object {
        val MSG_CLIENT_CONNECT = 1
        val MSG_CLIENT_DISCONNECT = 2
        val MSG_ADD_VALUE = 3
        val MSG_ADDED_VALUE = 4
    }

    //메신저 객체를 저장하는 리스트
    // -Messenger객체를 저장
    // -서비스에 바인딩되는 Activity 등에 객체를 전달해준다.
    // -서비스가 어떤 작업을 수행하고 그 결과 값을 전달해줄 때
    // 이 Messenger 객체를 통해서 엑티비티에 이벤트를 전달할 수 있다.
    /* 결과값을 엑티비티에 전달 */
    private val mClientCallbacks = ArrayList<Messenger>()

    //mMessenger
    //-엑티비티가 서비스에 이벤트를 전달할 때 사용하는 객체
    //-엑티비티가 서비스에 바인딩할 때 서비스는 mMessenger 객체의
    //Binder를 엑티비티에 전달해 준다.

    //CallbackHandler()
    // -엑티비티가 서비스로 전달한 이벤트들은 CallbackHandler 내부에서
    //처리된다.
    /* 엑티비티가 서비스에 이벤트를 전달 */
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
    }

    //엑티비티가 보낸 이벤트를 처리하는 핸들러
    inner class CallbackHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                //엑티비티에서 보낸 Messenger객체를 받아서 리스트에 추가한다.
                MSG_CLIENT_CONNECT -> {
                    Log.e(TAG, "Received MSG_CLIENT_CONNECT message from client")
                    mClientCallbacks.add(msg.replyTo)
                }
                //소켓 연결 해제
                MSG_CLIENT_DISCONNECT -> {
                    Log.e(TAG, "Received MSG_CLIENT_DISCONNECT message from client")
                    mClientCallbacks.remove(msg.replyTo)
                }
                /* Activity에 응답 값 보내기 */
                //엑티비티가 MSG_ADD_VALUE이벤트를 전달할 때 실행되는 메서드
                MSG_ADD_VALUE -> {
                    Log.e(TAG, "Received message from client: MSG_ADD_VALUE")
                    mValue += msg.arg1
                    var i: Int = mClientCallbacks.size - 1
                    while (i >= 0) {
                        try {
                            Log.e(TAG, "Send MSG_ADDED_VALUE message to client")
                            val added_msg: Message = Message.obtain(
                                null, RemoteService.MSG_ADDED_VALUE
                            )
                            added_msg.arg1 = mValue
                            mClientCallbacks.get(i).send(added_msg)
                        } catch (e: RemoteException) {
                            mClientCallbacks.removeAt(i)
                        }
                        i--
                    }
                }
            }
        }
    }


}