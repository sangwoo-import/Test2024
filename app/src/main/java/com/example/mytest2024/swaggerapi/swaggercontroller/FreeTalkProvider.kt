package com.example.mytest2024.swaggerapi.swaggercontroller

import com.example.mytest2024.swaggerapi.Retrofit.CommonResponseData
import com.example.mytest2024.swaggerapi.Retrofit.FreeTalkRequestData
import com.example.mytest2024.swaggerapi.Retrofit.FreeTalkResponse
import com.example.mytest2024.swaggerapi.Retrofit.RetrofitManager
import retrofit2.Response

class FreeTalkProvider(private var callback: CallBack) {
    fun FreeTalkGo(FreeTalkRequestData: FreeTalkRequestData) {

        RetrofitManager.service.freeTalk(freeTalkContent = FreeTalkRequestData)
            .enqueue(
                object : retrofit2.Callback<CommonResponseData<List<FreeTalkResponse>>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonResponseData<List<FreeTalkResponse>>>,
                        response: Response<CommonResponseData<List<FreeTalkResponse>>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                callback.completeFreeTalk(
                                    it.resultCode,
                                    it.resultMsg,
                                    it.resultData
                                )
                            }
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonResponseData<List<FreeTalkResponse>>>,
                        t: Throwable
                    ) {
                        print("서버 통신 실패 ")
                    }

                }
            )


    }


    interface CallBack {

        fun completeFreeTalk(
            code: String,
            msg: String,
            resultData: List<FreeTalkResponse>
        )


    }
}