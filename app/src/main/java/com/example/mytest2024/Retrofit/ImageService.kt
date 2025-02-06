package com.example.mytest2024.Retrofit

import retrofit2.http.GET
import retrofit2.http.Headers

interface ImageService {
    // Headers 에 권한 부여
    @Headers("Authorization: Client-ID 3OFcbUgiALg-iUJsR4xgJm1xY1lxacDWzOcvIRDdYno")
    @GET("photos/random")  // Request body 역할
    fun getRandomImage(): retrofit2.Call<ImageData>


}