package com.example.a220127_camera_1


import android.Manifest
import android.content.ContentValues
import android.content.Context
import androidx.databinding.DataBindingUtil


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a220127_camera_1.databinding.ActivityMainBinding

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri


import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.content.FileProvider
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //oncreate에서 호출해야함.
        //엑티비티에서 받아온 데이터 받기
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                //엑티비티에서 데이터를 갖고왔을 때만 실행
                if (it.resultCode == RESULT_OK) {
                    val file = File(currentPhotoPath)

                    //SDK 28버전 미만인 경우 getBitMap 사용
                    if (Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media
                            .getBitmap(contentResolver, Uri.fromFile(file))
                        binding.ivPhoto.setImageBitmap(bitmap)
                    } else {
                        //SDK 28버전 이상인 경우 setImageBitmap 사용
                        val decode = ImageDecoder.createSource(this.contentResolver,
                            Uri.fromFile(file.absoluteFile))
                        val bitmap = ImageDecoder.decodeBitmap(decode)
                        binding.ivPhoto.setImageBitmap(bitmap)
                        saveImageFile(file.name, getExtension(file.name), bitmap)
                    }
                }
            }




        binding.btnPhoto.setOnClickListener {
            when (it?.id) {
                binding.btnPhoto.id -> {
                    val agree_flag = settingPermission()

                    if (agree_flag == 1) {
                        startCapture()
                    } else if (agree_flag == 2) {

                    }

                }
            }

        }


    }

    //이미지의 확장자를 추출하는 메서드
    fun getExtension(fileStr: String): String {
        val fileExtension = fileStr.substring(fileStr.lastIndexOf(".") + 1, fileStr.length);
        return fileExtension
    }

    fun saveImageFile(filename: String, mimeType: String, bitmap: Bitmap): Uri? {
        //이미지 Uri 생성
        //contentValues는 ContentResolver가 사용하는 데이터 정보이다.
        var values = ContentValues()
        //contentValues의 이름, 타입을 정한다.
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // 파일 저장을 완료하기 전까지 다른 곳에서 해당 데이터를 요청하는 것을 무시
            values.put(MediaStore.Images.Media.IS_PENDING, 1)
        }


        // MediaStore에 파일 등록
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        try {
            if (uri != null) {
                // 파일 디스크립터 획득
                val descriptor = contentResolver.openFileDescriptor(uri, "w")
                if (descriptor != null) {
                    // FileOutputStream으로 비트맵 파일 저장. 숫자는 압축률
                    val fos = FileOutputStream(descriptor.fileDescriptor)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                    fos.close()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // 데이터 요청 무시 해제
                        values.clear()
                        values.put(MediaStore.Images.Media.IS_PENDING, 0)
                        contentResolver.update(uri, values, null, null)
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            Log.e("File", "error=")
        }
        return uri
    }


    //intent를 이용해서 카메라로 이동하는 함수
    fun startCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.a220127_camera_1.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    activityResultLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    //사진을 찍고 이미지를 파일로 저장해 주는 함수
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        //이미지 경로 지정
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    //카메라 관련 권한을 설정해주는 함수
    fun settingPermission(): Int {
        var agree_flag = 0
        val permis = object : PermissionListener {
            //  어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                agree_flag = 1
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                agree_flag = 2
            }

        }

        TedPermission.with(this)
            .setPermissionListener(permis)
            .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE).check()

        return agree_flag
    }


}