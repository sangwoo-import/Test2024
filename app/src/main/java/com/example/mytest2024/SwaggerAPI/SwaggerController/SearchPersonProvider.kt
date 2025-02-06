package com.example.mytest2024.SwaggerAPI.SwaggerController

import com.example.mytest2024.SwaggerAPI.Retrofit.CommonResponseData
import com.example.mytest2024.SwaggerAPI.Retrofit.RetrofitManager
import com.example.mytest2024.SwaggerAPI.Retrofit.SearchPerson
import com.example.mytest2024.SwaggerAPI.Retrofit.SearchPersonRequestData
import retrofit2.Response

class SearchPersonProvider(private var callback: CallBack) {

    fun SearchPersonGo(searchPersonRequestData: SearchPersonRequestData) {

        RetrofitManager.service.searchPerson(searchPersonStart = searchPersonRequestData)
            .enqueue(
                object : retrofit2.Callback<CommonResponseData<List<SearchPerson>>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonResponseData<List<SearchPerson>>>,
                        response: Response<CommonResponseData<List<SearchPerson>>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                callback.completeSearchPerson(
                                    it.resultCode,
                                    it.resultMsg,
                                    it.resultSize,
                                    it.resultData
                                )
                            }
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonResponseData<List<SearchPerson>>>,
                        t: Throwable
                    ) {
                        print("서버 통신 실패 ")
                    }

                }
            )


    }


    interface CallBack {

        fun completeSearchPerson(
            code: String,
            msg: String,
            resultSize :Int,
            resultData: List<SearchPerson>
        )


    }
}