package com.example.mytest2024.Controller


import com.example.mytest2024.Retrofit.ImageData
import com.example.mytest2024.Retrofit.RetrofitManager
import retrofit2.Response

class ImageProvider(private val callback: CallBack) {

    fun getRandomImage() {
        RetrofitManager.imageServie.getRandomImage()
            .enqueue(object : retrofit2.Callback<ImageData> {
                override fun onResponse(
                    call: retrofit2.Call<ImageData>,
                    response: Response<ImageData>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            callback.loadImage(it.urls.thumb)
                        }
                    }
                }
                override fun onFailure(call: retrofit2.Call<ImageData>, t: Throwable) {
                    println(t)
                }

            })
    }


    interface CallBack {
        //        fun loadImage(url:String, color:String)
        fun loadImage(url: String)
    }

}