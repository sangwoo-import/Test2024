package com.example.mytest2024.SwaggerAPI.SwaggerController

import com.example.mytest2024.SwaggerAPI.Retrofit.CommonResponseData
import com.example.mytest2024.SwaggerAPI.Retrofit.RetrofitManager
import com.example.mytest2024.SwaggerAPI.Retrofit.SearchPersonDetail
import com.example.mytest2024.SwaggerAPI.Retrofit.SearchPersonDetailRequestData
import retrofit2.Response

class SearchPersonDetailProvider(private var callback: CallBack) {
    fun SearchPersonDetailGo(searchPersonDetailRequestData: SearchPersonDetailRequestData) {

        RetrofitManager.service.searchPersonDetail(searchPersonDetail = searchPersonDetailRequestData)
            .enqueue(
                object : retrofit2.Callback<CommonResponseData<List<SearchPersonDetail>>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonResponseData<List<SearchPersonDetail>>>,
                        response: Response<CommonResponseData<List<SearchPersonDetail>>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                callback.completeSearchPersonDetail(
                                    it.resultCode,
                                    it.resultMsg,
                                    it.resultData
                                )
                            }
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonResponseData<List<SearchPersonDetail>>>,
                        t: Throwable
                    ) {
                        print("서버 통신 실패 ")
                    }

                }
            )


    }


    interface CallBack {

        fun completeSearchPersonDetail(
            code: String,
            msg: String,
            resultData: List<SearchPersonDetail>
        )


    }

}