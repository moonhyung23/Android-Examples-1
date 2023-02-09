package com.example.a211216_firestore_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.a211216_firestore_1.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //실시간 감지
        firestore.collection("exCollection3")
            .document("sampleDoc")
            //데이터가 변경되었는지를 실시간으로 감지한다.
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("tag", "listen:error", e)
                    return@addSnapshotListener
                }
                //선택한 요청에 따라서 다른 메서드를 호출
                if (snapshot != null && snapshot.exists()) {
                    //스냅샷의 데이터를 갖고온다.
                    val data = snapshot.data
                    Log.e("itemId", data?.get("sdpMid") as String)
                    Log.e("itemId2", data["serverUrl"] as String)
                }
            }
        //조회
          FirebaseFirestore.getInstance().collection("exCollection").get()
              .addOnCompleteListener { task ->
                  //task.result -> firestore에서 가져온 레코드
                  //fireStore에서 가져온 레코드의 개수만큼 반복
                  for (item in task.result!!.documents) {
                      Log.e("itemId", item.id)
                      Log.e("itemId", item["sdpMid"] as String)
                      Log.e("itemId", item["serverUrl"] as String)
                  }
              }
        //저장
        binding.btnAdd.setOnClickListener {
            val resultDTO = ResultDTO()
            resultDTO.sdpCandidate = "sdp3"
            resultDTO.sdpMLineIndex = 2
            resultDTO.sdpMid = "sdpmid22"
            resultDTO.serverUrl = "serverUrl22"
            resultDTO.type = "offer22"
            firestore.collection("exCollection3").document("sampleDoc")
                .set(resultDTO)
        }
    }


}