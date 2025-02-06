package com.example.mytest2024.SwaggerAPI.SwaggerController

import com.example.mytest2024.SwaggerAPI.Retrofit.CommonResponseData
import com.example.mytest2024.SwaggerAPI.Retrofit.FileListData
import com.example.mytest2024.SwaggerAPI.Retrofit.LetterDetailRequestData
import com.example.mytest2024.SwaggerAPI.Retrofit.LetterDetailResponse
import com.example.mytest2024.SwaggerAPI.Retrofit.LetterRequestData
import com.example.mytest2024.SwaggerAPI.Retrofit.LetterResponse
import com.example.mytest2024.SwaggerAPI.Retrofit.RetrofitManager
import retrofit2.Response
import java.io.File

class LetterDetailProvider(private var callback: CallBack) {

    fun letterDetailGo(letterDetailRequestData: LetterDetailRequestData) {

        RetrofitManager.service.letterDetailContent(letterDetailContent = letterDetailRequestData)
            .enqueue(
                object : retrofit2.Callback<CommonResponseData<List<LetterDetailResponse>>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonResponseData<List<LetterDetailResponse>>>,
                        response: Response<CommonResponseData<List<LetterDetailResponse>>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {


                                val fileListData = it.resultData.flatMap { letterDetail ->
                                    letterDetail.fileList
                                }

                                callback.completeLetterDetail(
                                    it.resultCode,
                                    it.resultMsg,
                                    it.resultData,
                                    fileListData
                                )
                            }
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonResponseData<List<LetterDetailResponse>>>,
                        t: Throwable
                    ) {
                        print("서버 통신 실패 ")
                    }

                }
            )
    }

    interface CallBack {
        fun completeLetterDetail(
            code: String,
            msg: String,
            resultData: List<LetterDetailResponse>,
            fileListData: List<FileListData>
        )
    }
}