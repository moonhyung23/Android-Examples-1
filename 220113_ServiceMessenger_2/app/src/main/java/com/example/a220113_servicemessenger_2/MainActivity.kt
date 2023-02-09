package com.example.a220113_servicemessenger_2

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.a220113_servicemessenger_2.databinding.ActivityMainBinding
import android.os.Bundle


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"


    /*Messen객체가 2개*/
    //Service에 전달받은 Messenger 객체
    //이 객체를 통해 서비스에 요청을 보낼 수 있다.
    private var mServiceCallback: Messenger? = null

    //콜백핸들러를 등록해서 서비스에서 오는 응답을 받을 수 있다.
    //mClientCallback객체를 서비스에 보내서 서비스에서 보내는 응답을 받을 수 있게 한다.
    private val mClientCallback: Messenger = Messenger(CallbackHandler())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val user = User("문형", 26)
        binding.user = user


        binding.btnAddValue.setOnClickListener {
            if (mServiceCallback != null) {
                //숫자 10과 함께 MSG_ADD_VALUE이벤트를 서비스에 전달
                val msg: Message = Message.obtain(
                    null, RemoteService.MSG_ADD_VALUE)
                msg.arg1 = 10
                try {
                    mServiceCallback?.send(msg)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
                Log.e(TAG, "Send MSG_ADD_VALUE message to Service")
            }
        }

        //엑티비티 이동
        binding.btnMove.setOnClickListener {
            val intent = Intent(applicationContext, SubActivity::class.java)
            startActivity(intent)
            finish()
        }

        //어플 실행하면 바인드 서비스 바로  시작
        Log.e(TAG, "Trying to connect to service")
        val intent = Intent(applicationContext, RemoteService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    //Service를 binding
    //bindind이 되면 ServiceConnection으로 IBinder객체가 전달되고
    //IBinder객체로 Messenger객체를 만들수  있다.
    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.e(TAG, "onServiceConnected")
            //서비스에 바인딩 되었을 때 생성되는 Ibinder객체(service)로 Messenger객체를 생성해준다.
            //Messenger객체는 서비스에 이벤트를 전달해준다.
            mServiceCallback = Messenger(service)

            //binding으로 서비스로부터 Messenger객체를 받았고
            //그 객체를 통해 엑티비티의 Messenger객체를 서비스에
            //전달한다.
            // connect to service
            val connect_msg = Message.obtain(null, RemoteService.MSG_CLIENT_CONNECT)
            //서비스에 전달할 Messenger객체
            connect_msg.replyTo = mClientCallback
            val bundle = Bundle()
            bundle.putString(RemoteService.BUNDLEKEY, "MainActivity")
            connect_msg.data = bundle

            try {
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