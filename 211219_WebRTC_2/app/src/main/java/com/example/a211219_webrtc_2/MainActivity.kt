package com.example.a211219_webrtc_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Constants.isIntiatedNow = true
        Constants.isCallEnded = true
        //영상통화 방 만들기.
        start_meeting.setOnClickListener {
            //방이름이 입력되지 않은 경우
            if (meeting_id.text.toString().trim().isEmpty())
                meeting_id.error = "Please enter meeting id"
            else {
                //입력된 경우
                db.collection("calls")
                    //방이름을 통해서 firestore에 document 생성
                    .document(meeting_id.text.toString())
                    .get()
                    .addOnSuccessListener {
                        //이미  만들어진 방이 있는 경우
                        if (it["type"] == "OFFER" || it["type"] == "ANSWER" || it["type"] == "END_CALL") {
                            //실패
                            meeting_id.error = "Please enter new meeting ID"
                        }
                        //만들어진 방이 없는 경우
                        else {
                            //성공
                            val intent = Intent(this@MainActivity, RTCActivity::class.java)
                            //입력한 방제목 intent에 저장
                            intent.putExtra("meetingID", meeting_id.text.toString())
                            //false -> 요청(offer)
                            intent.putExtra("isJoin", false)
                            //화상채팅 엑티비티로 이동
                            startActivity(intent)
                        }
                    }
                    //실패
                    .addOnFailureListener {
                        meeting_id.error = "Please enter new meeting ID"
                    }
            }
        }

        //방 입장 버튼
        join_meeting.setOnClickListener {
            if (meeting_id.text.toString().trim().isNullOrEmpty())
                meeting_id.error = "Please enter meeting id"
            else {
                val intent = Intent(this@MainActivity, RTCActivity::class.java)
                //입력한 방제목 intent에 저장
                intent.putExtra("meetingID", meeting_id.text.toString())
                //true -> 응답(answer)
                intent.putExtra("isJoin", true)
                //화상채팅 엑티비티로 이동
                startActivity(intent)
            }
        }
    }
}