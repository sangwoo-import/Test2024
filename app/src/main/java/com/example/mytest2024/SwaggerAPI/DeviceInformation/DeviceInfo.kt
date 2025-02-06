package com.example.mytest2024.SwaggerAPI.DeviceInformation

import android.content.Context
import android.os.Build
import android.provider.Settings

class DeviceInfo(val context: Context) {

    fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID).toString()

    }

    fun getDeviceOs(): String {
        return Build.VERSION.RELEASE.toString()
    }

    fun getDeviceModel(): String{
        return Build.MODEL.toString()
    }
}