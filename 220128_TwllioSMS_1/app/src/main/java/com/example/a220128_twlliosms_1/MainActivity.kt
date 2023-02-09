package com.example.a220128_twlliosms_1

import android.view.View


import androidx.databinding.DataBindingUtil


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.a220128_twlliosms_1.databinding.ActivityMainBinding

import com.google.android.gms.auth.api.credentials.Credential

import com.google.android.gms.tasks.OnFailureListener

import com.google.android.gms.tasks.OnSuccessListener

import com.google.android.gms.auth.api.phone.SmsRetriever

import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityMainBinding

    val ACCOUNT_SID = System.getenv("ACf423edf53bba23378dd5d6b96432a0c4")
    val AUTH_TOKEN = System.getenv("fe3441ed8a9106dfc43f511cea9ee4d2")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        //버튼클릭 리스너 등록
//        binding.버튼1ID.setOnClickListener(this)

        //oncreate에서 호출해야함.
        //엑티비티에서 받아온 데이터 받기
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
//                if (requestCode == RESOLVE_HINT) {

                //엑티비티에서 데이터를 갖고왔을 때만 실행
                if (it.resultCode == RESULT_OK) {

                    val credential: Credential? =
                        it.data?.getParcelableExtra(Credential.EXTRA_KEY)
//                     will need to process phone number string
                    credential?.id;
                }
//                }
            }

        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        val client = SmsRetriever.getClient(this /* context */)
        val task: Task<Void> = client.startSmsRetriever()
        //성공
        task.addOnSuccessListener(OnSuccessListener<Void?> {
            // Successfully started retriever, expect broadcast intent
            // ...
        })
        //실패
        task.addOnFailureListener(OnFailureListener {
            // Failed to start retriever, inspect Exception for more details
            // ...
        })
    }



    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1) 버튼1
            binding.btnSend.id -> {
                /*   Twilio.init(ACCOUNT_SID, AUTH_TOKEN)
                   val message = Message.creator(
                       PhoneNumber("+01049041537"),
                       PhoneNumber("+17755229860"),
                       "Where's Wallace?")
                       .create()
                   println(message.sid)*/
            }
            else -> {
            }
        }
    }


}