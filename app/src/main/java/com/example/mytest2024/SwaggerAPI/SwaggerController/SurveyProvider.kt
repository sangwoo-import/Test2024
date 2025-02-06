package com.example.mytest2024.SwaggerAPI.SwaggerController

import com.example.mytest2024.SwaggerAPI.Retrofit.CommonResponseData
import com.example.mytest2024.SwaggerAPI.Retrofit.LetterRequestData
import com.example.mytest2024.SwaggerAPI.Retrofit.LetterResponse
import com.example.mytest2024.SwaggerAPI.Retrofit.RetrofitManager
import com.example.mytest2024.SwaggerAPI.Retrofit.SurveyRequestData
import com.example.mytest2024.SwaggerAPI.Retrofit.SurveyResponse
import retrofit2.Response

class SurveyProvider(private var callback: CallBack) {
    fun surveyGo(surveyRequestData: SurveyRequestData) {

        RetrofitManager.service.surveyContent(surveyContent = surveyRequestData)
            .enqueue(
                object : retrofit2.Callback<CommonResponseData<List<SurveyResponse>>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonResponseData<List<SurveyResponse>>>,
                        response: Response<CommonResponseData<List<SurveyResponse>>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                callback.completeSurvey(
                                    it.resultCode,
                                    it.resultMsg,
                                    it.resultData
                                )
                            }
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonResponseData<List<SurveyResponse>>>,
                        t: Throwable
                    ) {
                        print("서버 통신 실패 ")
                    }

                }
            )


    }


    interface CallBack {

        fun completeSurvey(
            code: String,
            msg: String,
            resultData: List<SurveyResponse>
        )


    }
}