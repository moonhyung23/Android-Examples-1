package com.example.a220113_servicemessenger_2

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.a220113_servicemessenger_2.databinding.ActivityMainBinding
import com.example.a220113_servicemessenger_2.databinding.ActivitySubBinding

class SubActivity : AppCompatActivity() {
    /*Messen객체가 2개*/
    //Service에 전달받은 Messenger 객체
    //이 객체를 통해 서비스에 요청을 보낼 수 있다.
    private var mServiceCallback: Messenger? = null

    //콜백핸들러를 등록해서 서비스에서 오는 응답을 받을 수 있다.
    private val mClientCallback: Messenger = Messenger(CallbackHandler())


    private lateinit var binding: ActivitySubBinding
    private val TAG = "SubActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)


        //서비스 시작
        Log.e(TAG, "Trying to connect to service")
        val intent = Intent(applicationContext, RemoteService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.e(TAG, "onServiceConnected")
            //서비스에 바인딩 되었을 때 생성되는 Ibinder객체(service)로 Messenger객체를 생성해준다.
            //Messenger객체는 서비스에 이벤트를 전달해준다.
            mServiceCallback = Messenger(service)

            // connect to service
            val connect_msg = Message.obtain(null, RemoteService.MSG_CLIENT_CONNECT)

            //서비스에 전달할 Messenger객체
            connect_msg.replyTo = mClientCallback
            val bundle = Bundle()
            bundle.putString(RemoteService.BUNDLEKEY, "SubActivity")
            connect_msg.data = bundle

            try {
                //서비스에 요청 보내기
                mServiceCallback?.send(connect_msg)
                Log.e(TAG, "Send MSG_CLIENT_CONNECT message to Service")
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        //서비스 연결 종료
        override fun onServiceDisconnected(name: ComponentName) {
            Log.e(TAG, "onServiceDisconnected")
            mServiceCallback = null
        }
    }

    //서버응답을 처리하는 핸들러
    inner class CallbackHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                //
                RemoteService.MSG_ADDED_VALUE -> Log.e(TAG,
                    "서버응답_Recevied MSG_ADDED_VALUE message from service ~ value :" + msg.arg1)

                //소켓연결응답 리스너
                RemoteService.MSG_SocketCon_VALUE -> Log.e(TAG,
                    "서버응답_2" + msg.arg1)
            }
        }
    }
}