package com.example.mytest2024.swaggerapi.swaggercontroller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mytest2024.swaggerapi.Retrofit.LoginRequestData
import com.example.mytest2024.swaggerapi.Retrofit.LoginResponse
import com.example.mytest2024.swaggerapi.Retrofit.CommonResponseData
import com.example.mytest2024.swaggerapi.Retrofit.RetrofitManager
import retrofit2.Response

class LoginProvider() :ViewModel() {

    private val _getLoginResponse = MutableLiveData<CommonResponseData<List<LoginResponse>>>()

    val getLoginResponse : LiveData<CommonResponseData<List<LoginResponse>>> = _getLoginResponse

    fun sendLogin(loginRequestData: LoginRequestData)  {


        RetrofitManager.service.sendLogin(loginRequestStart = loginRequestData)
            .enqueue(
                object : retrofit2.Callback<CommonResponseData<List<LoginResponse>>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonResponseData<List<LoginResponse>>>,
                        response: Response<CommonResponseData<List<LoginResponse>>>
                    ) {
                        if (response.isSuccessful) {
                           _getLoginResponse.value = response.body()
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

//    interface CallBack {
//
//        fun completeLogin(
//            code: String,
//            msg: String,
//            resultData: List<LoginResponse>
//
//        )
//
//
//    }
}