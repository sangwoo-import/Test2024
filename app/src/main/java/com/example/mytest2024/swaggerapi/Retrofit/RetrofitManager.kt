package com.example.mytest2024.swaggerapi.Retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitManager {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .addNetworkInterceptor(HeaderInterceptor())
        .build()

    // gson 사용
    private val gson = GsonBuilder().setLenient().create()

    //Retrofit 사용
    private val retrofit = Retrofit.Builder().baseUrl("http://192.168.200.98:31000/GBITR_MS/")
        .addConverterFactory(GsonConverterFactory.create(gson)) // gson 생성
        .client(okHttpClient)
        .build()

    // 실제 사용될 이미지 서비스
    val service : RetrofitService by lazy { retrofit.create(RetrofitService::class.java) }

}

