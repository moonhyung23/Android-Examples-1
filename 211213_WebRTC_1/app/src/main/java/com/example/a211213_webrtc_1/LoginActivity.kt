package com.example.a211213_webrtc_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.a211213_webrtc_1.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.jar.Manifest

class LoginActivity : AppCompatActivity() {
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var binding: ActivityLoginBinding
    var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)


        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id2))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //oncreate에서 호출해야함.
        //엑티비티에서 받아온 데이터 받기
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result: ActivityResult ->
                //엑티비티에서 데이터를 갖고왔을 때만 실행
                if (result.resultCode == RESULT_OK) {
                    val data: Intent? = result.data
                    //API에서 토큰 값을 intent를 통해서 받아온다.
                    // -token은 email이 암호화된 값이다.
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    val account = task.getResult(ApiException::class.java)
                    //API 토큰을 이용해서 credential 변수를 만들어준다.
                    val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
                    //credential 변수를 FireBase에 넣는다.
                    //FireBase는 credential 변수를 받아서 회원가입이 될 수 있게 해준다.
                    FirebaseAuth.getInstance().signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                println("로그인 성공")
                                //로그인 할 때 입력한 정보를 db에 입력
                                saveUserDataToDatabase(task.result.user)
                            }
                        }
                }
            }


        //구글 로그인 버튼
        binding.loginBtn.setOnClickListener {
            Log.e("MainActivity", "aaa")
            googleSignIn()

        }

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO),
            0
        )
    }

    fun googleSignIn() {
        //구글 로그인 인증 엑티비티로 이동
        val i: Intent? = googleSignInClient?.signInIntent
        activityResultLauncher.launch(i)
    }

    fun saveUserDataToDatabase(user: FirebaseUser?) {
        val email = user?.email
        val uid = user?.uid

        var userDTO = UserDTO()
        userDTO.email = email

        //DataBase인 FireStore안에있는 컬렉션에 usersDTO 값을 저장
        FirebaseFirestore.getInstance().collection("users").document(uid!!).set(userDTO)
        //로그인 성공 시 메인 엑티비티로 이동
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}