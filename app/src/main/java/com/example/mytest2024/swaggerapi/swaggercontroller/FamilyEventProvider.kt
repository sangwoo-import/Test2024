package com.example.mytest2024.swaggerapi.swaggercontroller

import com.example.mytest2024.swaggerapi.Retrofit.CommonResponseData
import com.example.mytest2024.swaggerapi.Retrofit.FamilyEventRequestData
import com.example.mytest2024.swaggerapi.Retrofit.FamilyEventResponse
import com.example.mytest2024.swaggerapi.Retrofit.RetrofitManager
import retrofit2.Response

class FamilyEventProvider(private var callback: CallBack) {
    fun FamilyEventGo(familyEventRequestData: FamilyEventRequestData) {

        RetrofitManager.service.familyEvent(familyEventTalkContent = familyEventRequestData)
            .enqueue(
                object : retrofit2.Callback<CommonResponseData<List<FamilyEventResponse>>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonResponseData<List<FamilyEventResponse>>>,
                        response: Response<CommonResponseData<List<FamilyEventResponse>>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                callback.completeFamilyEvent(
                                    it.resultCode,
                                    it.resultMsg,
                                    it.resultData
                                )
                            }
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonResponseData<List<FamilyEventResponse>>>,
                        t: Throwable
                    ) {
                        print("서버 통신 실패 ")
                    }

                }
            )


    }


    interface CallBack {

        fun completeFamilyEvent(
            code: String,
            msg: String,
            resultData: List<FamilyEventResponse>
        )


    }
}