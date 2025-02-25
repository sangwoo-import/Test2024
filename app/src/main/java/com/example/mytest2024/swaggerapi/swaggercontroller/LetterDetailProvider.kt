package com.example.mytest2024.swaggerapi.swaggercontroller

import android.annotation.SuppressLint
import androidx.databinding.Observable
import androidx.databinding.PropertyChangeRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.mytest2024.swaggerapi.Retrofit.CommonResponseData
import com.example.mytest2024.swaggerapi.Retrofit.FileListData
import com.example.mytest2024.swaggerapi.Retrofit.LetterDetailRequestData
import com.example.mytest2024.swaggerapi.Retrofit.LetterDetailResponse
import com.example.mytest2024.swaggerapi.Retrofit.RetrofitManager
import retrofit2.Response
import java.io.File

class LetterDetailProvider : ViewModel(), Observable {


    private val _getLetterDetailResponse =
        MutableLiveData<CommonResponseData<List<LetterDetailResponse>>>()

    val getLetterDetailResponse: LiveData<CommonResponseData<List<LetterDetailResponse>>> =
        _getLetterDetailResponse


    private val _letterDetail = MutableLiveData<LetterDetailResponse>()
    val letterDetail: LiveData<LetterDetailResponse> = _letterDetail


    private val _letterDetail_fileName = MutableLiveData<FileListData>()
    val letterDetailFileName: LiveData<FileListData> = _letterDetail_fileName


    fun letterDetailGo(letterDetailRequestData: LetterDetailRequestData) {

        RetrofitManager.service.letterDetailContent(letterDetailContent = letterDetailRequestData)
            .enqueue(
                object : retrofit2.Callback<CommonResponseData<List<LetterDetailResponse>>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonResponseData<List<LetterDetailResponse>>>,
                        response: Response<CommonResponseData<List<LetterDetailResponse>>>
                    ) {
                        if (response.isSuccessful) {
                            _getLetterDetailResponse.value = response.body()

                            _letterDetail.value =
                                _getLetterDetailResponse.value?.resultData?.firstOrNull()

//                            if(_letterDetail.value?.fileList.isNullOrEmpty()){
//                                _letterDetail_fileName.postValue(null)
//                            }

                            _letterDetail_fileName.value = _letterDetail.value?.fileList?.firstOrNull()


                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonResponseData<List<LetterDetailResponse>>>,
                        t: Throwable
                    ) {
//                        _getLetterDetailResponse.postValue(null)
                    }

                }
            )
    }


    private val callbacks: PropertyChangeRegistry = PropertyChangeRegistry()

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callback?.let { callbacks.add(it) }
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
        callback?.let { callbacks.remove(it) }
    }
    fun notifyChange() {
        callbacks.notifyCallbacks(this, 0, null)
    }
}