package com.example.a220127_retrofit_2

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface APIS {


    @POST(MyApp.Example_url)
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    //post로 서버에 데이터를 보내는 메서드
    fun post_users(
        @Body postModel: PostModel,
    ): Call<PostModel>

    @FormUrlEncoded
    @POST(MyApp.Example_url)
    @Headers(
        "accept: application/json",
        "content-type: application/x-www-form-urlencoded; charset=utf-8"
    )
    //post로 서버에 데이터를 보내는 메서드
    fun post_users(
        // 서버에 Post방식으로 보낼 떄 사용하는  파라미터의 키 값
        //ex)@Field('키') =>  $_POST['키']
        @Field("id") nick: String,
        @Field("nick") pwd: String,
        @Field("pwd") id: String,
    ): Call<PostModel>


    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        //서버 IP만 입력해주세요~
        private const val BASE_URL = "http://3.37.253.243"
        fun create(): APIS {
            val gson: Gson = GsonBuilder().setLenient().create();
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(createOkHttpClient())
                .build()
                .create(APIS::class.java)
        }

        private fun createOkHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
            return builder.build()
        }
    }


}