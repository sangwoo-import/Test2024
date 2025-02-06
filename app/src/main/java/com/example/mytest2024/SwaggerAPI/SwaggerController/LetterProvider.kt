package com.example.mytest2024.SwaggerAPI.SwaggerController

import com.example.mytest2024.SwaggerAPI.Retrofit.CommonResponseData
import com.example.mytest2024.SwaggerAPI.Retrofit.LetterRequestData
import com.example.mytest2024.SwaggerAPI.Retrofit.LetterResponse
import com.example.mytest2024.SwaggerAPI.Retrofit.RetrofitManager
import retrofit2.Response

class LetterProvider(private var callback: CallBack) {

    fun letterGo(letterRequestData: LetterRequestData) {

        RetrofitManager.service.letterContent(letterContent = letterRequestData)
            .enqueue(
                object : retrofit2.Callback<CommonResponseData<List<LetterResponse>>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonResponseData<List<LetterResponse>>>,
                        response: Response<CommonResponseData<List<LetterResponse>>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                callback.completeLetter(
                                    it.resultCode,
                                    it.resultMsg,
                                    it.resultData
                                )
                            }
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonResponseData<List<LetterResponse>>>,
                        t: Throwable
                    ) {
                        print("서버 통신 실패 ")
                    }

                }
            )


    }


    interface CallBack {

        fun completeLetter(
            code: String,
            msg: String,
            resultData: List<LetterResponse>
        )


    }
}