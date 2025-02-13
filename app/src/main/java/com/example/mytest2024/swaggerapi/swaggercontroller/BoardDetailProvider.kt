package com.example.mytest2024.swaggerapi.swaggercontroller

import com.example.mytest2024.swaggerapi.Retrofit.BoardCommentListData
import com.example.mytest2024.swaggerapi.Retrofit.BoardDetailRequestData
import com.example.mytest2024.swaggerapi.Retrofit.BoardDetailResponse
import com.example.mytest2024.swaggerapi.Retrofit.BoardFileListData
import com.example.mytest2024.swaggerapi.Retrofit.CommonResponseData
import com.example.mytest2024.swaggerapi.Retrofit.RetrofitManager
import retrofit2.Response

class BoardDetailProvider(private var callback: CallBack) {


    fun boardDetailGo(boardDetailRequestData: BoardDetailRequestData) {

        RetrofitManager.service.boardDetailContent(boardDetailContent = boardDetailRequestData)
            .enqueue(
                object : retrofit2.Callback<CommonResponseData<List<BoardDetailResponse>>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonResponseData<List<BoardDetailResponse>>>,
                        response: Response<CommonResponseData<List<BoardDetailResponse>>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {

                                val boardFileListData = it.resultData.flatMap { boardDetail ->
                                    boardDetail.boardFileList
                                }


                                val boardCommentList = it.resultData.flatMap { boardDetail ->
                                    boardDetail.boardCommentList

                                }


                                callback.completeBoardDetail(
                                    it.resultCode,
                                    it.resultMsg,
                                    it.resultData,
                                    boardFileListData,
                                    boardCommentList,
                                )
                            }
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonResponseData<List<BoardDetailResponse>>>,
                        t: Throwable
                    ) {
                        print("서버 통신 실패 ")
                    }

                }
            )
    }



    interface CallBack {
        fun completeBoardDetail(
            code: String,
            msg: String,
            resultData: List<BoardDetailResponse>,
            boardFileListData: List<BoardFileListData>,
            boardCommentList: List<BoardCommentListData>,
        )
    }
}