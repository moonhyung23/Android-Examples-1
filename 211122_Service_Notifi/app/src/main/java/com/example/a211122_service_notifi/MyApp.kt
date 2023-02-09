package com.example.a211122_service_notifi

import android.app.Application
import android.content.Context

class MyApp : Application() {
    var email: String? = null
    var pw: String? = null
    var pw_check: String? = null
    var nick: String? = null
    var birth_date: String? = null


    /*주소관련 변수*/
    //거주지역
    var live_addr: String? = ""

    //관심지역
    var interest_addr: String? = ""


    /*거주지, 관심지역 구분
    * 1번 -> 거주지
    * 2번 -> 관심지역
    * */
    var addr_flag: Int = 0

    //RegisterActivity에 사용하는 키 값
    //변수가 입력되었는지 입력되지 않았는지를 구분
    var KEY_RESULT_FLAG = "RESULT_FLAG"


    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
    }


    companion object {
        lateinit var instance: MyApp
    }

    //Context -> 어느 엑티비티에서도 컨텍스트를 받아올 수 있음
    fun context(): Context {
        return instance.applicationContext
    }

}



