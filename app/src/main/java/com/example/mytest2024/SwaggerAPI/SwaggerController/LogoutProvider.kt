package com.example.mytest2024.SwaggerAPI.SwaggerController

import com.example.mytest2024.SwaggerAPI.Retrofit.RetrofitManager
import com.example.mytest2024.SwaggerAPI.Retrofit.LogoutRequestData
import com.example.mytest2024.SwaggerAPI.Retrofit.LogoutResponseData
import retrofit2.Response

class LogoutProvider(private val callback: CallBack) {


    fun sendLogout(logoutRequestData: LogoutRequestData) {

        RetrofitManager.service.sendLogout(logoutRequestStart = logoutRequestData)
            .enqueue(
                object : retrofit2.Callback<LogoutResponseData> {
                    override fun onResponse(
                        call: retrofit2.Call<LogoutResponseData>,
                        response: Response<LogoutResponseData>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                callback.completeLogout(
                                    it.resultCode,
                                    it.resultMsg,
                                )
                            }
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<LogoutResponseData>,
                        t: Throwable
                    ) {
                        print("서버 통신 실패 ")
                    }

                }
            )


    }


    interface CallBack {

        fun completeLogout(
            code: String,
            msg: String,
        )


    }
}