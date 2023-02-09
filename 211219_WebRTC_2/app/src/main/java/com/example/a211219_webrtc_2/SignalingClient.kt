package com.example.a211219_webrtc_2

import kotlinx.coroutines.ExperimentalCoroutinesApi
import android.os.Build
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import org.json.JSONObject
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription

//코루틴 클래스
@ExperimentalCoroutinesApi
class SignalingClient(
    private val meetingID: String,
    private val listener: SignalingClientListener
) : CoroutineScope {

    companion object {
        private const val HOST_ADDRESS = "192.168.0.12"
    }


    private val job = Job()

    val TAG = "SignallingClient"

    val db = Firebase.firestore

    private val gson = Gson()

    var SDPtype: String? = null
    override val coroutineContext = Dispatchers.IO + job

    private val sendChannel = ConflatedBroadcastChannel<String>()

    //시그널링 연결
    init {
        connect()
    }

    private fun connect() = launch {
        //FireStore db연결
        db.enableNetwork().addOnSuccessListener {
            listener.onConnectionEstablished()
            Log.e("SignailngClient", "58")
        }

        val sendData = sendChannel.offer("")
        sendData.let {
            Log.v(this@SignalingClient.javaClass.simpleName, "Sending: $it")
        }

        try {
            //calls
            db.collection("calls")
                .document(meetingID)
                .addSnapshotListener { snapshot, e ->
                    //에러
                    if (e != null) {
                        Log.e("SignailngClient", "85")
                        Log.w(TAG, "listen:error", e)
                        return@addSnapshotListener
                    }
                    //파이어스토어에 Offer가 저장된 경우
                    if (snapshot != null && snapshot.exists()) {
                        //파이어 스토어에 저장된 SDP 정보를 갖고온다.
                        val data = snapshot.data
                        //type이 'offer'인 경우
                        if (data?.containsKey("type")!! &&
                            data.getValue("type").toString() == "OFFER"
                        ) {
                            //fireStore DB에 SDP가 Offer 타입으로
                            //저장되었는지를 감지
                            listener.onOfferReceived(
                                //저장된 경우 SeesionDescripon에 추가
                                SessionDescription(
                                    SessionDescription.Type.OFFER, data["sdp"].toString()
                                )
                            )
                            SDPtype = "Offer"

                        }
                        //Answer
                        else if (data.containsKey("type") &&
                            data.getValue("type").toString() == "ANSWER"
                        ) {
                            //파이어스토어에 'Answer'가 저장 되었는지를 감지
                            listener.onAnswerReceived(
                                //저장된 경우 SeesionDescripon에 추가
                                SessionDescription(
                                    SessionDescription.Type.ANSWER, data["sdp"].toString()
                                )
                            )
                            SDPtype = "Answer"

                        }
                        //종료 버튼
                        else if (!Constants.isIntiatedNow && data.containsKey("type") &&
                            data.getValue("type").toString() == "END_CALL"
                        ) {
                            listener.onCallEnded()
                            SDPtype = "End Call"

                        }
                        Log.d(TAG, "Current data: ${snapshot.data}")
                    } else {
                        //예외처리 data가 없는 경우
                        Log.d(TAG, "Current data: null")
                    }
                }
            db.collection("calls").document(meetingID)
                .collection("candidates").addSnapshotListener { querysnapshot, e ->
                    //에러
                    if (e != null) {
                        Log.e("SignailngClient", "143")
                        Log.e(TAG, "listen:error", e)
                        return@addSnapshotListener
                    }
                    //ICE 후보가 추가된 경우를 감지
                    if (querysnapshot != null && !querysnapshot.isEmpty) {
                        for (dataSnapShot in querysnapshot) {
                            val data = dataSnapShot.data
                            //Offer를 ICE후보로 추가
                            if (SDPtype == "Offer" && data.containsKey("type") && data.get("type") == "offerCandidate") {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    listener.onIceCandidateReceived(
                                        IceCandidate(
                                            data["sdpMid"].toString(),
                                            Math.toIntExact(data["sdpMLineIndex"] as Long),
                                            data["sdpCandidate"].toString()
                                        )
                                    )
                                }
                            }
                            //Answer를 ICE후보로 추가
                            else if (SDPtype == "Answer" && data.containsKey("type") && data.get("type") == "answerCandidate") {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    listener.onIceCandidateReceived(
                                        IceCandidate(
                                            data["sdpMid"].toString(),
                                            Math.toIntExact(data["sdpMLineIndex"] as Long),
                                            data["sdpCandidate"].toString()
                                        )
                                    )
                                }
                                Log.e("SignailngClient", "176")
                            }
                        }
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "connectException: $e")
        }
    }

    //파이어스토어에 ICE 후보를 추가하는 베서드
    fun sendIceCandidate(candidate: IceCandidate?, isJoin: Boolean) = runBlocking {
        //타입 구분
        val type = when {
            isJoin -> "answerCandidate"
            else -> "offerCandidate"
        }
        //추가할 ICE 정보
        val candidateConstant = hashMapOf(
            "serverUrl" to candidate?.serverUrl,
            "sdpMid" to candidate?.sdpMid,
            "sdpMLineIndex" to candidate?.sdpMLineIndex,
            "sdpCandidate" to candidate?.sdp,
            "type" to type
        )

        db.collection("calls")
            .document("$meetingID").collection("candidates").document(type)
            .set(candidateConstant as Map<String, Any>)
            .addOnSuccessListener {
                //파이어스토어에 후보추가 성공
                Log.e(TAG, "sendIceCandidate: Success")
            }
            .addOnFailureListener {
                //파이어 스토어에 후보추가 실패
                Log.e(TAG, "sendIceCandidate: Error $it")
            }
    }


    fun destroy() {
        Log.e("SignailngClient", "239")
//        client.close()
        job.complete()
    }
}