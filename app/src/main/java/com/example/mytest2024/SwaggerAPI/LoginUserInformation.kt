package com.example.mytest2024.SwaggerAPI

object LoginUserInformation {

    var userSn: String? = null
    var userName: String? = null
    var wardName: String? = null
    var className: String? = null
    var deviceId: String? = null
    fun saveUserInfo(
        userName: String,
        wardName: String,
        className: String,
        userSn: String) {
        this.userName = userName
        this.wardName = wardName
        this.className = className
        this.userSn = userSn

    }
    fun saveDeviceInfo(deviceId : String){
        this.deviceId = deviceId
    }
}