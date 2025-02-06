package com.example.mytest2024.SwaggerAPI.SwaggerController

import com.example.mytest2024.SwaggerAPI.Retrofit.LoginRequestData
import com.example.mytest2024.SwaggerAPI.Retrofit.LoginResponse
import com.example.mytest2024.SwaggerAPI.Retrofit.CommonResponseData
import com.example.mytest2024.SwaggerAPI.Retrofit.RetrofitManager
import retrofit2.Response

class LoginProvider(private val callback: CallBack) {


    fun sendLogin(loginRequestData: LoginRequestData) {

        RetrofitManager.service.sendLogin(loginRequestStart = loginRequestData)
            .enqueue(
                object : retrofit2.Callback<CommonResponseData<List<LoginResponse>>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonResponseData<List<LoginResponse>>>,
                        response: Response<CommonResponseData<List<LoginResponse>>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                callback.completeLogin(
                                    it.resultCode,
                                    it.resultMsg,
                                    it.resultData
                                )
                            }
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonResponseData<List<LoginResponse>>>,
                        t: Throwable
                    ) {
                        print("서버 통신 실패 ")
                    }

                }
            )


    }

    interface CallBack {

        fun completeLogin(
            code: String,
            msg: String,
            resultData: List<LoginResponse>

        )


    }
}