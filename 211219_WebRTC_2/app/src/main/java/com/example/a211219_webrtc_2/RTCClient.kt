package com.example.a211219_webrtc_2

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.webrtc.*


/* Offer와 Answer를 생성한 후
*  LocalDescription 혹은 RemoteDescription에 저장
*  하고 FireStore DB에 저장하는 메서드
*
* */

class RTCClient(
    context: Application,
    observer: PeerConnection.Observer
) {
    //
    companion object {
        private const val LOCAL_TRACK_ID = "local_track"
        private const val LOCAL_STREAM_ID = "local_track"
    }

    private val rootEglBase: EglBase = EglBase.create()
    private var localAudioTrack: AudioTrack? = null
    private var localVideoTrack: VideoTrack? = null
    val TAG = "RTCClient"

    //상대방에게 받을 session
    var remoteSessionDescription: SessionDescription? = null

    val db = Firebase.firestore

    //피어커넥션 초기화
    init {
        initPeerConnectionFactory(context)
    }

    /* "stun:stun.l.google.com:19302",
     "stun:stun1.l.google.com:19302",
     "stun:stun2.l.google.com:19302",
     "stun:stun3.l.google.com:19302",
     "stun:stun4.l.google.com:19302",
     "stun:stun.vodafone.ro:3478",
     "stun:stun.samsungsmartcam.com:3478",
     "stun:stun.services.mozilla.com:3478"*/
    //stunServer
    private val iceServer = listOf(
        PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302")
            .createIceServer()
    )

    //영상에 필요한 소스
    private val peerConnectionFactory by lazy { buildPeerConnectionFactory() }
    private val videoCapturer by lazy { getVideoCapturer(context) }
    private val audioSource by lazy { peerConnectionFactory.createAudioSource(MediaConstraints()) }
    private val localVideoSource by lazy { peerConnectionFactory.createVideoSource(false) }
    private val peerConnection by lazy { buildPeerConnection(observer) }

    //PeerConnection 세팅
    private fun initPeerConnectionFactory(context: Application) {
        val options = PeerConnectionFactory.InitializationOptions.builder(context)
            .setEnableInternalTracer(true)
            .setFieldTrials("WebRTC-H264HighProfile/Enabled/")
            .createInitializationOptions()
        PeerConnectionFactory.initialize(options)
    }


    //피어커넥션을 생성할 수 있는 객체를 반환
    //피어 커넥션 객체를 반환
    private fun buildPeerConnectionFactory(): PeerConnectionFactory {
        return PeerConnectionFactory
            .builder()
            .setVideoDecoderFactory(DefaultVideoDecoderFactory(rootEglBase.eglBaseContext))
            .setVideoEncoderFactory(
                DefaultVideoEncoderFactory(
                    rootEglBase.eglBaseContext,
                    true,
                    true
                )
            )
            .setOptions(PeerConnectionFactory.Options().apply {
                disableEncryption = true
                disableNetworkMonitor = true
            })
            .createPeerConnectionFactory()
    }

    //피어커넥션 객체 생성
    private fun buildPeerConnection(observer: PeerConnection.Observer) =
        peerConnectionFactory.createPeerConnection(
            iceServer,
            observer
        )

    //
    private fun getVideoCapturer(context: Context) =
        Camera2Enumerator(context).run {
            deviceNames.find {
                isFrontFacing(it)
            }?.let {
                createCapturer(it, null)
            } ?: throw IllegalStateException()
        }

    //WebRTC 화면 View 초기화
    fun initSurfaceView(view: SurfaceViewRenderer) = view.run {
        setMirror(true)
        setEnableHardwareScaler(true)
        init(rootEglBase.eglBaseContext, null)
    }

    //비디오 화면 시작
    fun startLocalVideoCapture(localVideoOutput: SurfaceViewRenderer) {
        val surfaceTextureHelper =
            SurfaceTextureHelper.create(Thread.currentThread().name, rootEglBase.eglBaseContext)
        (videoCapturer as VideoCapturer).initialize(
            surfaceTextureHelper,
            localVideoOutput.context,
            localVideoSource.capturerObserver
        )
        videoCapturer.startCapture(320, 240, 60)
        localAudioTrack =
            peerConnectionFactory.createAudioTrack(LOCAL_TRACK_ID + "_audio", audioSource);
        localVideoTrack = peerConnectionFactory.createVideoTrack(LOCAL_TRACK_ID, localVideoSource)
        localVideoTrack?.addSink(localVideoOutput)
        val localStream = peerConnectionFactory.createLocalMediaStream(LOCAL_STREAM_ID)
        localStream.addTrack(localVideoTrack)
        localStream.addTrack(localAudioTrack)
        peerConnection?.addStream(localStream)
    }

    //처음 offer를 생성하는 메서드
    //offer를 생성 후에 LocalDescription에 추가 후 FireStore에 저장
    private fun PeerConnection.call(sdpObserver: SdpObserver, meetingID: String) {
        val constraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
        }

        //offer를 생성
        createOffer(object : SdpObserver by sdpObserver {
            //offer 생성 성공
            override fun onCreateSuccess(desc: SessionDescription?) {
                //생성한 offer를 LocalDescription에 추가.
                setLocalDescription(object : SdpObserver {
                    //offer LocalDescription에 저장 실패
                    override fun onSetFailure(p0: String?) {
                        //실패
                        Log.e(TAG, "onSetFailure: $p0")
                    }

                    //offer LocalDescription에 저장 성공
                    override fun onSetSuccess() {
                        //sdp, type 유형
                        val offer = hashMapOf(
                            "sdp" to desc?.description,
                            "type" to desc?.type
                        )
                        //Answer에게 전달해줄 offer를 DB에 저장
                        db.collection("calls").document(meetingID)
                            .set(offer)
                            //fireStore에 저장 성공
                            .addOnSuccessListener {
                                Log.e("RTClient", "149")
                                Log.e(TAG, "DocumentSnapshot added")
                            }
                            //fireStore에 저장 실패
                            .addOnFailureListener { e ->
                                Log.e("RTClient", "154")
                                Log.e(TAG, "Error adding document", e)
                            }
                    }

                    //Offer 생성 성공
                    override fun onCreateSuccess(p0: SessionDescription?) {
                        Log.e(TAG, "onCreateSuccess: Description $p0")
                    }

                    //Offer생성 실패
                    override fun onCreateFailure(p0: String?) {
                        Log.e(TAG, "onCreateFailure: $p0")
                    }
                }, desc)
                sdpObserver.onCreateSuccess(desc)
            }

        }, constraints)

    }


    //Offer를 받은후에 Answer를 생성하는 메서드
    private fun PeerConnection.answer(sdpObserver: SdpObserver, meetingID: String) {
        val constraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
        }
        //Answer를 생성한다.
        createAnswer(object : SdpObserver by sdpObserver {
            //생성 성공
            override fun onCreateSuccess(desc: SessionDescription?) {
                //offer를 응답으로 바꾸기
                val answer = hashMapOf(
                    "sdp" to desc?.description,
                    "type" to desc?.type
                )

                //db에 저장할 answer
                db.collection("calls").document(meetingID)
                    .set(answer)
                    .addOnSuccessListener {
                        //파이어스토어에 저장성공
                        Log.e(TAG, "DocumentSnapshot added")
                    }

                    .addOnFailureListener { e ->
                        //파이어스토어에 저장실패
                        Log.e(TAG, "Error adding document", e)
                    }

                //LocalDescription에 생성한 Answer를 저장한다.
                setLocalDescription(object : SdpObserver {
                    //LocalDescription에 저장 실패
                    override fun onSetFailure(p0: String?) {
                        Log.e(TAG, "onSetFailure: $p0")
                    }

                    //LocalDescription에 저장 성공
                    override fun onSetSuccess() {
                        Log.e(TAG, "onSetSuccess")
                    }

                    //Answer저장 성공
                    override fun onCreateSuccess(p0: SessionDescription?) {
                        Log.e("RTClient", "230")
                        Log.e(TAG, "onCreateSuccess: Description $p0")
                    }

                    //Answer저장 실패
                    override fun onCreateFailure(p0: String?) {
                        Log.e("RTClient", "235")
                        Log.e(TAG, "onCreateFailureLocal: $p0")
                    }
                }, desc)
                //옵져버에
                sdpObserver.onCreateSuccess(desc)
            }
        }, constraints)
    }

    //offer생성 후 db에 저장
    fun call(sdpObserver: SdpObserver, meetingID: String) =
        //-peerConnection을 초기화
        peerConnection?.call(sdpObserver, meetingID)


    //Answer 생성 후 db에 저장
    fun answer(sdpObserver: SdpObserver, meetingID: String) =
        peerConnection?.answer(sdpObserver, meetingID)

    //받은 Offer를 RemoteDescription에 추가할 때 사용
    fun onRemoteSessionReceived(sessionDescription: SessionDescription) {
        //받은 offer
        remoteSessionDescription = sessionDescription
        //받은 offer를 RemoteDescription에 추가한다.
        peerConnection?.setRemoteDescription(object : SdpObserver {
            //추가 실패
            override fun onSetFailure(p0: String?) {
                Log.e(TAG, "onSetFailure: $p0")
            }

            //추가 성공
            override fun onSetSuccess() {
                Log.e(TAG, "onSetSuccessRemoteSession")
            }

            override fun onCreateSuccess(p0: SessionDescription?) {
                Log.e(TAG, "onCreateSuccessRemoteSession: Description $p0")
            }

            override fun onCreateFailure(p0: String?) {
                Log.e(TAG, "onCreateFailure")
            }
        }, sessionDescription)
    }

    fun addIceCandidate(iceCandidate: IceCandidate?) {
        Log.e("RTClient", "289")
        peerConnection?.addIceCandidate(iceCandidate)
    }

    //연결종료
    fun endCall(meetingID: String) {
        //연결된 candidates를 찾는다.
        db.collection("calls").document(meetingID).collection("candidates")
            .get().addOnSuccessListener {
                //조회 성공
                val iceCandidateArray: MutableList<IceCandidate> = mutableListOf()

                for (dataSnapshot in it) {
                    //offerCandidates가 있으면
                    if (dataSnapshot.contains("type") && dataSnapshot["type"] == "offerCandidate") {
                        val offerCandidate = dataSnapshot
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            //offerCandidates를 candidates를 리스트에 담는다.
                            iceCandidateArray.add(
                                IceCandidate(
                                    offerCandidate["sdpMid"].toString(),
                                    Math.toIntExact(offerCandidate["sdpMLineIndex"] as Long),
                                    offerCandidate["sdp"].toString()
                                )
                            )
                        }
                    }
                    //remoteCandidates가 있으면
                    else if (dataSnapshot.contains("type") && dataSnapshot["type"] == "answerCandidate") {
                        val answerCandidate = dataSnapshot
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            //remoteCandidates를 담는다.
                            iceCandidateArray.add(
                                IceCandidate(
                                    answerCandidate["sdpMid"].toString(),
                                    Math.toIntExact(answerCandidate["sdpMLineIndex"] as Long),
                                    answerCandidate["sdp"].toString()
                                )
                            )
                        }
                    }
                }
                //FireStore에서 candidates삭제
                peerConnection?.removeIceCandidates(iceCandidateArray.toTypedArray())
            }
        val endCall = hashMapOf(
            "type" to "END_CALL"
        )
        db.collection("calls").document(meetingID)
            .set(endCall)
            .addOnSuccessListener {
                Log.e(TAG, "DocumentSnapshot added")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error adding document", e)
            }
        //커넥션 종료
        peerConnection?.close()
    }

    //비디오 끄기
    fun enableVideo(videoEnabled: Boolean) {
        if (localVideoTrack != null)
            localVideoTrack?.setEnabled(videoEnabled)
    }

    //오디오 끄기
    fun enableAudio(audioEnabled: Boolean) {
        if (localAudioTrack != null)
            localAudioTrack?.setEnabled(audioEnabled)
    }

    //카메라 전환
    fun switchCamera() {
        videoCapturer.switchCamera(null)
    }

}