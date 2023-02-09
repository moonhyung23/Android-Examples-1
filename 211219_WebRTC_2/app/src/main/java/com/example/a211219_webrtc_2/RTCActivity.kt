package com.example.a211219_webrtc_2

import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import kotlinx.coroutines.ExperimentalCoroutinesApi
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_rtcactivity.*
import org.webrtc.*
import java.util.*


class RTCActivity : AppCompatActivity() {

    //권한 관련 변수
    companion object {
        //카메라 오디오 권한 요청 코드
        private const val CAMERA_AUDIO_PERMISSION_REQUEST_CODE = 1

        //카메라 권한
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA

        //오디오 권한
        private const val AUDIO_PERMISSION = Manifest.permission.RECORD_AUDIO
    }

    private lateinit var rtcClient: RTCClient
    private lateinit var signallingClient: SignalingClient
    private val audioManager by lazy { RTCAudioManager.create(this) }

    val TAG = "RTCActivity"

    private var meetingID: String = "test-call"

    private var isJoin = false

    private var isMute = false

    private var isVideoPaused = false

    private var inSpeakerMode = true


    private val sdpObserver = object : AppSdpObserver() {
        override fun onCreateSuccess(p0: SessionDescription?) {
            super.onCreateSuccess(p0)
//            signallingClient.send(p0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rtcactivity)
        if (intent.hasExtra("meetingID"))
        //meetingID를 갖고온다. -> 방제목
            meetingID = intent.getStringExtra("meetingID")!!
        //방 참여자 정보 갖고오기 OFFER/ANSWER .
        if (intent.hasExtra("isJoin"))
            isJoin = intent.getBooleanExtra("isJoin", false)

        //카메라, 오디오 권한 체크
        checkCameraAndAudioPermission()

        /*WebRTC 옵션 관련 코드 */

        audioManager.selectAudioDevice(RTCAudioManager.AudioDevice.SPEAKER_PHONE)
        //카메라 전환 (거울모드 <-> 기본모드)
        switch_camera_button.setOnClickListener {
            rtcClient.switchCamera()
            Log.e("RTCActivity", "78")
        }

        //스피커 켜기, 끄기 버튼
        audio_output_button.setOnClickListener {
            Log.e("RTCActivity", "84")
            //스피커가 켜진 경우 스피커 끄기
            if (inSpeakerMode) {
                Log.e("RTCActivity", "87")
                inSpeakerMode = false
                audio_output_button.setImageResource(R.drawable.ic_baseline_hearing_24)
                audioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.EARPIECE)
            }
            //스피커가 꺼진 경우 스피커 키기
            else {
                Log.e("RTCActivity", "94")
                inSpeakerMode = true
                audio_output_button.setImageResource(R.drawable.ic_baseline_speaker_up_24)
                audioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.SPEAKER_PHONE)
            }
        }

        //비디오 끄기 켜기 버튼
        video_button.setOnClickListener {
            if (isVideoPaused) {
                isVideoPaused = false
                video_button.setImageResource(R.drawable.ic_baseline_videocam_off_24)
            } else {
                isVideoPaused = true
                video_button.setImageResource(R.drawable.ic_baseline_videocam_24)
            }
            rtcClient.enableVideo(isVideoPaused)
        }

//화상채팅 종료 버튼
        end_call_button.setOnClickListener {
            rtcClient.endCall(meetingID)
            remote_view.isGone = false
            Constants.isCallEnded = true
            finish()
            //처음 화면으로 이동
            startActivity(Intent(this@RTCActivity, MainActivity::class.java))
        }


    }


    //카메라 권한 체크
    private fun checkCameraAndAudioPermission() {
        if ((ContextCompat.checkSelfPermission(this, CAMERA_PERMISSION)
                    != PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission(this, AUDIO_PERMISSION)
                    != PackageManager.PERMISSION_GRANTED)
        ) {

            //카메라 권한요청
            requestCameraAndAudioPermission()
        } else {
            //카메라 오디오 권한이 있는 경우 실행되는 코드
            onCameraAndAudioPermissionGranted()
        }
    }

    //카메라 권한이 있는 경우 실행
    private fun onCameraAndAudioPermissionGranted() {
        //옵저버 등록
        rtcClient = RTCClient(
            application,
            object : PeerConnectionObserver() {
                //
                override fun onIceCandidate(p0: IceCandidate?) {
                    super.onIceCandidate(p0)
                    //파이어 스토어에 ICE 정보를 추가한다.
                    signallingClient.sendIceCandidate(p0, isJoin)
                    rtcClient.addIceCandidate(p0)
                }

                //비디오 스트림을 추가
                override fun onAddStream(p0: MediaStream?) {
                    super.onAddStream(p0)
                    Log.e(TAG, "onAddStream: $p0")
                    p0?.videoTracks?.get(0)?.addSink(remote_view)
                }

                override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {
                    Log.e(TAG, "onIceConnectionChange: $p0")
                }

                override fun onIceConnectionReceivingChange(p0: Boolean) {
                    Log.e(TAG, "onIceConnectionReceivingChange: $p0")
                }

                override fun onConnectionChange(newState: PeerConnection.PeerConnectionState?) {
                    Log.e(TAG, "onConnectionChange: $newState")
                }

                override fun onDataChannel(p0: DataChannel?) {
                    Log.e(TAG, "onDataChannel: $p0")
                }

                override fun onStandardizedIceConnectionChange(newState: PeerConnection.IceConnectionState?) {
                    Log.e(TAG, "onStandardizedIceConnectionChange: $newState")
                }

                override fun onAddTrack(p0: RtpReceiver?, p1: Array<out MediaStream>?) {
                    Log.e(TAG, "onAddTrack: $p0 \n $p1")
                }

                override fun onTrack(transceiver: RtpTransceiver?) {
                    Log.e(TAG, "onTrack: $transceiver")
                }
            }
        )
        //상대방 화면 세팅
        rtcClient.initSurfaceView(remote_view)
        //내 화면 세팅
        rtcClient.initSurfaceView(local_view)
        //내 카메라 ??
        rtcClient.startLocalVideoCapture(local_view)
        //시그널링 클라이언트 코루틴 클래스 생성
        //옵저버 등록
        signallingClient = SignalingClient(meetingID, createSignallingClientListener())
        //false -> client 요청 (offer)
        if (!isJoin)
        //Offer인 경우 옵저버 등록
            rtcClient.call(sdpObserver, meetingID)
    }

    private fun createSignallingClientListener() = object : SignalingClientListener {
        override fun onConnectionEstablished() {
            //통화종료
            end_call_button.isClickable = true
        }

        //받은 offer를 remoteDescripton에 추가하는 메서드
        override fun onOfferReceived(description: SessionDescription) {
            rtcClient.onRemoteSessionReceived(description)
            Constants.isIntiatedNow = false
            rtcClient.answer(sdpObserver, meetingID)
            remote_view_loading.isGone = true
        }

        //받은 Answer를 Offer가 RemoteDescription에 추가
        override fun onAnswerReceived(description: SessionDescription) {
            rtcClient.onRemoteSessionReceived(description)
            Constants.isIntiatedNow = false
            remote_view_loading.isGone = true
        }

        //ICE 추가
        override fun onIceCandidateReceived(iceCandidate: IceCandidate) {
            rtcClient.addIceCandidate(iceCandidate)
        }

        //화상통화 종료
        override fun onCallEnded() {
            if (!Constants.isCallEnded) {
                Constants.isCallEnded = true
                rtcClient.endCall(meetingID)
                finish()
                startActivity(Intent(this@RTCActivity, MainActivity::class.java))
            }
        }
    }

    //카메라 오디오 권한 요청
    private fun requestCameraAndAudioPermission(dialogShown: Boolean = false) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA_PERMISSION) &&
            ActivityCompat.shouldShowRequestPermissionRationale(this, AUDIO_PERMISSION) &&
            !dialogShown
        ) {
            showPermissionRationaleDialog()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(CAMERA_PERMISSION, AUDIO_PERMISSION),
                CAMERA_AUDIO_PERMISSION_REQUEST_CODE
            )
        }
    }

    //권한 요청 다이얼로그
    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("Camera And Audio Permission Required")
            .setMessage("This app need the camera and audio to function")
            .setPositiveButton("Grant") { dialog, _ ->
                dialog.dismiss()
                requestCameraAndAudioPermission(true)
            }
            .setNegativeButton("Deny") { dialog, _ ->
                dialog.dismiss()
                onCameraPermissionDenied()
            }
            .show()
    }

    //카메라 권한 거절
    private fun onCameraPermissionDenied() {
        Toast.makeText(this, "Camera and Audio Permission Denied", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        signallingClient.destroy()
        super.onDestroy()
    }
}