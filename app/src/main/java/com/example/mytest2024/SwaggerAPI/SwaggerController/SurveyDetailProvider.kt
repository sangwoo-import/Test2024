package com.example.mytest2024.SwaggerAPI.SwaggerController

import com.example.mytest2024.SwaggerAPI.Retrofit.CommonResponseData
import com.example.mytest2024.SwaggerAPI.Retrofit.RetrofitManager
import com.example.mytest2024.SwaggerAPI.Retrofit.SurveyDetailRequestData
import com.example.mytest2024.SwaggerAPI.Retrofit.SurveyDetailResponse
import com.example.mytest2024.SwaggerAPI.Retrofit.SurveyQuestionList
import retrofit2.Response

class SurveyDetailProvider(private var callback: CallBack) {

    fun surveyDetailGo(surveyDetailRequestData: SurveyDetailRequestData) {

        RetrofitManager.service.surveyDetailContent(surveyDetailContent = surveyDetailRequestData)
            .enqueue(
                object : retrofit2.Callback<CommonResponseData<List<SurveyDetailResponse>>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonResponseData<List<SurveyDetailResponse>>>,
                        response: Response<CommonResponseData<List<SurveyDetailResponse>>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {

                                /* 질문 항목 들*/
                                val surveyQuestionList = it.resultData.flatMap { surveyDetail ->
                                    surveyDetail.questionList

                                }

                                callback.completeSurveyDetail(
                                    it.resultCode,
                                    it.resultMsg,
                                    it.resultData,
                                    surveyQuestionList,
                                )
                            }
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonResponseData<List<SurveyDetailResponse>>>,
                        t: Throwable
                    ) {
                        print("서버 통신 실패 ")
                    }

                }
            )
    }


    interface CallBack {
        fun completeSurveyDetail(
            code: String,
            msg: String,
            resultData: List<SurveyDetailResponse>,
            surveyQuestionList: List<SurveyQuestionList>,
        )
    }
}