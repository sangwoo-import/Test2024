package com.example.mytest2024.Retrofit

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Time
import java.util.concurrent.TimeUnit

object RetrofitManager {


    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    // gson 사용
    private val gson = GsonBuilder().setLenient().create()

    //Retrofit 사용
    private val retrofit = Retrofit.Builder().baseUrl("https://api.unsplash.com/")
        .addConverterFactory(GsonConverterFactory.create(gson)) // gson 생성
        .client(okHttpClient)
        .build()

    // 실제 사용될 이미지 서비스
     val imageServie : ImageService by lazy { retrofit.create(ImageService::class.java) }


}

