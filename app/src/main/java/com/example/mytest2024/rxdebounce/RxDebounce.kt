package com.example.mytest2024.rxdebounce

import android.util.Log
import android.widget.EditText
import com.example.mytest2024.swaggerapi.Retrofit.SearchPersonRequestData
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.schedulers.Timed
import java.util.concurrent.TimeUnit

class RxDebounce {
    private val disposables = CompositeDisposable() // Disposable 관리


    /* rx java */
    fun EditText.setQueryDebounce(queryFunction: (String)->Unit): Disposable {
        val editTextChangeObservable = this.textChanges()
        val searchEditTextSubscription: Disposable =
            editTextChangeObservable
                // 마지막 글자 입력 0.8초 후에 onNext 이벤트로 데이터 발행
                .debounce(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                // 구독을 통해 이벤트 응답 처리
                .subscribeBy(
                    onNext = {
                        //Timber.d("onNext : $it")
                        queryFunction(it.toString())
                    },
                    onComplete = {
                        //Timber.d("onComplete")
                    },
                    onError = {
                        //Timber.i("onError : $it")
                    }
                )
        return searchEditTextSubscription  // Disposable 반환
    }

    fun clear() {
        disposables.clear() // 메모리 누수 방지
    }

}