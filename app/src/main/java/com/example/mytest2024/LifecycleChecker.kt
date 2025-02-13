package com.example.mytest2024

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.mytest2024.screenlock.BioAuthManager

class LifecycleChecker : Application(), LifecycleEventObserver {

    private val bioAuthManager = BioAuthManager


    companion object {
        private var TAG = "LifeCycleShow"

        var isBackground = false

        // 현재 실행 중인 액티비티를 추적하기 위한 변수
        var currentActivity: FragmentActivity? = null

    }

    override fun onCreate() {
        super.onCreate()

        // 생명 주기 관찰 등록
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)


        // 현재 실행 중인 Activity 추적
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {
                if (activity is FragmentActivity) {
                    if (activity !is SplashActivity) {
                        currentActivity = activity


                    }

                }
            }

            override fun onActivityPaused(activity: Activity) {
                if (activity == currentActivity) {
                    currentActivity = null
                }
            }

            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })


    }


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {


        Log.d("tag_lc", "targetState : ${event.targetState} event : $event")
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                Log.d(TAG, " ON_CREATE 임")
            }

            Lifecycle.Event.ON_START -> {
                Log.d(TAG, "ON_START 임")


            }

            Lifecycle.Event.ON_RESUME -> {
                Log.d(TAG, "ON_RESUME 임")

                /*포그라운드*/
                isBackground = false

                val bioAuth = bioAuthManager.getInstance()

                val sharedPref = getSharedPreferences("LockCheck", Context.MODE_PRIVATE)
                val lockCheck = sharedPref.getBoolean("check", false)


                // 현재 실행 중인 액티비티가 있을 때만 인증 수행

                if (lockCheck) {
                    currentActivity?.let { activity ->
                        bioAuth.authenticate(activity, activity.applicationContext)
                    } ?: Log.e(TAG, "현재 실행 중인 액티비티가 없음")

                }


            }

            Lifecycle.Event.ON_PAUSE -> {
                Log.d(TAG, "ON_PAUSE 임")
                // 백그라운드
                isBackground = true
            }

            Lifecycle.Event.ON_STOP -> {
                Log.d(TAG, "ON_STOP 임")
            }

            Lifecycle.Event.ON_DESTROY -> {
                Log.d(TAG, "ON_DESTROY 임")

                // 생명 주기 관찰 해제
                // ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
            }

            Lifecycle.Event.ON_ANY -> {
                Log.d(TAG, "onStateChanged ON_ANY")
            }


            else -> {}
        }
    }
}