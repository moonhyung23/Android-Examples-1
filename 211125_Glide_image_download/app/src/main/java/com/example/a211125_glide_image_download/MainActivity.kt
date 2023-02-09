package com.example.a211125_glide_image_download

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.a211125_glide_image_download.databinding.ActivityMainBinding
import androidx.core.app.ActivityCompat.startActivityForResult

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.SystemClock
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import androidx.annotation.NonNull

import android.R.attr.data
import android.R.attr.bitmap
import android.content.ContentValues.TAG

import android.os.Environment
import android.util.Log
import com.bumptech.glide.request.transition.Transition
import android.graphics.BitmapFactory
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

import android.os.AsyncTask

import java.net.MalformedURLException
import java.net.URL
import java.io.*
import java.net.HttpURLConnection


class MainActivity : AppCompatActivity(), View.OnClickListener {
    var context: Context? = null
    var msg_success_img_download = "성공_이미지다운로드"
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    //기본 이미지 경로
    var Base_img_url: String = "http://3.37.253.243/sports_friend/app_image/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        context = applicationContext
        //읽기 쓰기 권한 허용
        var per_image = Permission_image(this)
        per_image.checkPer()


        //이미지 갖고오기
        binding.btnGetImage.setOnClickListener(this)
        binding.iv1Download.setOnClickListener(this)


        Permission_image(applicationContext)

        //모집 글 이미지가 없는 경우
        Glide.with(applicationContext)
            .load(Base_img_url + "IMG-61244e0481a4f1.41482151.jpg")
            .override(500, 500)
            .into(binding.iv)

        /* intent를 통해 선택한 이미지를 uri로 변환하는 코드 */
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                //엑티비티에서 데이터를 갖고왔을 때만 실행
                if (it.resultCode == RESULT_OK) {

                    it.data?.data?.let { uri ->
                        //이미지 uri
                        val imageUri: Uri? = it.data?.data
                        //이미지 uri가 null 이 아닐때만 리스트에 추가.
                        if (imageUri != null) {
                            Glide.with(this).load(imageUri).override(500, 500).into(binding.iv)
                        }
                    }
                }
            }

    }

    //버튼클릭 리스너 등록

    //버튼 클릭 리스너 메서드
    override fun onClick(v: View?) {
        when (v?.id) {
            //1) 이미지 갖고오기
            binding.btnGetImage.id -> {
                //이미지의 URI를 intent를 사용해 가져오는 코드
                var intent = Intent(Intent.ACTION_PICK)
                intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                intent.type = "image/*"
                //엑티비티이동
                activityResultLauncher.launch(intent)
            }
            //2) 이미지 다운
            binding.iv1Download.id -> {
                val str_result =
                    DownloadFilesTask().execute(Base_img_url + "IMG-61244e0481a4f1.41482151.jpg")
                        .get()


                if (str_result == msg_success_img_download) {
                    Toast.makeText(applicationContext, "이미지 다운로드 성공", Toast.LENGTH_SHORT).show()
                }
            }


            else -> {
            }
        }
    }


    //파일 읽기, 쓰기 권한
    class Permission_image(context1: Context) {
        var context: Context = context1

        var permissionlistener: PermissionListener = object : PermissionListener {
            // 어떤 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성합니다  object : 객체명
            override fun onPermissionGranted() {
                // 권한 허가시 실행 할 내용

            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                // 권한 거부시 실행  할 내용
            }
        }

        // 권한 체크하는 메소드
        // 매개변수로 객체를 받아서 체크를 해볼까??
        fun checkPer() {
            TedPermission.with(context)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("앱의 기능을 사용하기 위해서는 권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .check()
        }
    }

    class DownloadFilesTask : AsyncTask<String, Void, String>() {
        var bmp: Bitmap? = null
        var img_url: String? = null
        var result = ""
        override fun doInBackground(vararg params: String?): String {
            try {
                //이미지 url
                img_url = params[0]
                //이미지 url -> 비트맵으로 변환
                bmp = img_url?.let { loadImage(it) }
                if (bmp != null) {
                    //이미지 저장
                    result = save_image_dir(bmp, "IMG-61244e0481a4f1.41482151.jpg")
                } else {
                    result = "실패_비트맵변환실패"
                }


            } catch (e: MalformedURLException) {
                result = "실패_MalformedURLException"
            } catch (e: IOException) {
                result = "실패_IOException_DoinBackGround"
            }

            return result;
        }


        override fun onPostExecute(result: String?) {

        }

        fun loadImage(imageUrl: String): Bitmap? {
            var bitmap: Bitmap? = null
            val connection: HttpURLConnection?
            try {
                val url = URL(imageUrl)
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET" //request 방식 설정
                connection.connectTimeout = 10000  // 10초의 타임아웃
                connection.doOutput = true // OutPutStream으로 데이터를 넘겨주겠다고 설정
                connection.doInput = true  // InputStream으로 데이터를 읽겠다는 설정
                connection.useCaches = true // 캐싱 여부
                connection.connect()
                val resCode = connection.responseCode //연결 상태 확인

                if (resCode == HttpURLConnection.HTTP_OK) { // 200 일때 bitmap으로 변경
                    val input =
                        connection.inputStream
                    bitmap = BitmapFactory.decodeStream(input)
                    // BitmapFactory의 메소드를 통해 InputStream으로부터 Bitmap을 만들어 준다.
                    connection.disconnect()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return bitmap
        }

        fun save_image_dir(bitmap: Bitmap?, fileName: String): String {
            var result = ""
            // 파일 경로 생성, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM): 공개 디렉터리에 저장하는 기능
            val saveDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString()
            val filePath = File(saveDir)

            // 파일이 없을 경우 생성
            if (!filePath.mkdir()) {
                filePath.mkdir()
            }
            try {

                // 이미지 파일 생성 (파일 경로, 지정할 파일명)
                val imgFile = File(filePath, fileName)
                // 파일을 쓸 수 있는 스트림을 준비합니다.
                val out = FileOutputStream(imgFile)
                // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                // 스트림 사용후 닫아줍니다.
                out.close()
                result = "성공_이미지다운로드"
            } catch (e: FileNotFoundException) {
                result = "실패_파일을 찾을 수 없음"
                Log.e(TAG, "FileNotFoundException : ")
            } catch (e: IOException) {
                result = "실패_IOExeption"
                Log.e(TAG, "IOException : ")
            }

            return result
        }
    }

}









