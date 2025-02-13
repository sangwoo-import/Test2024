package com.example.mytest2024.swaggerapi.swaggercontroller

import com.example.mytest2024.swaggerapi.Retrofit.RetrofitManager
import com.example.mytest2024.swaggerapi.Retrofit.SurveyAnswerSaveRequestData
import com.example.mytest2024.swaggerapi.Retrofit.SurveySaveAnswerResponse
import retrofit2.Response

class SurveySaveAnswerProvider(private var callback: CallBack) {
    fun surveySaveAnswerGo(surveySaveAnswerRequestData: SurveyAnswerSaveRequestData) {



        RetrofitManager.service.surveySaveAnswerContent(surveySaveAnswerContent = surveySaveAnswerRequestData)
            .enqueue(
                object : retrofit2.Callback<SurveySaveAnswerResponse> {
                    override fun onResponse(
                        call: retrofit2.Call<SurveySaveAnswerResponse>,
                        response: Response<SurveySaveAnswerResponse>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                callback.completeSurveySaveAnswer(
                                    it.resultCode,
                                    it.resultMsg,
                                )
                            }
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<SurveySaveAnswerResponse>,
                        t: Throwable
                    ) {
                        print("서버 통신 실패 ")
                    }

                }
            )


    }


    interface CallBack {

        fun completeSurveySaveAnswer(
            code: String,
            msg: String,

            )


    }
}