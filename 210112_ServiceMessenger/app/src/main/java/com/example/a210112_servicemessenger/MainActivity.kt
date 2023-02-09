package com.example.a210112_servicemessenger

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.content.Intent
import android.content.ComponentName

import android.content.ServiceConnection
import android.os.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    /*Messen객체가 2개*/
    //1)서비스로 부터 전달받은 Messenger객체
    //-위의 서비스코드에서 바인딩할 때 던져주는 IBinder로 만들어진
    //Messenger객체
    //-서비스에 있는 메서드나 멤버를 사용하기 위한 객체
    private var mServiceCallback: Messenger? = null

    //2)서비스로 전달해주는 Messenger 객체
    //-서비스에서 엑티비티로 결과를 리턴하기 위해 사용되는 Messenger객체
    //2-1)CallbackHandler
    //CallbackHandler객체를 생성자에 전달
    //-서비스가 엑티비티로 이벤트를 전달하면 Messenger객체는 이벤트를 CallbackHandler에
    //던져준다.
    //서비스에서 보낸 이벤트를 처리하기 위해 필요한 객체
    private val mClientCallback: Messenger = Messenger(CallbackHandler())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.btn_add_value)
        //버튼을 누르면 서비스로 부터 이벤트를 전달
        button.setOnClickListener {
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

        Log.e(TAG, "Trying to connect to service")
        val intent = Intent(applicationContext, RemoteService::class.java)
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    //Service를 binding
    //bindind이 되면 ServiceConnection으로 IBinder객체가 전달되고
    //그것으로 Messenger객체를 만들수  있다.
    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.e(TAG, "onServiceConnected")
            //서비스에 바인딩 되었을 때 생성되는 Ibinder객체(service)
            //로 Messenger객체를 생성해준다.
            //Messenger객체는 서비스에 이벤트를 전달해준다.
            mServiceCallback = Messenger(service)

            //binding으로 서비스로부터 Messenger객체를 받았고
            //그 객체를 통해 엑티비티의 Messenger객체를 서비스에
            //전달한다.
            // connect to service
            val connect_msg = Message.obtain(null, RemoteService.MSG_CLIENT_CONNECT)
            //서비스에 전달할 Messenger객체
            connect_msg.replyTo = mClientCallback
            try {
                mServiceCallback?.send(connect_msg)
                Log.e(TAG, "Send MSG_CLIENT_CONNECT message to Service")
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.e(TAG, "onServiceDisconnected")
            mServiceCallback = null
        }
    }

    inner class CallbackHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                RemoteService.MSG_ADDED_VALUE -> Log.e(TAG,
                    "Recevied MSG_ADDED_VALUE message from service ~ value :" + msg.arg1)
            }
        }
    }

}