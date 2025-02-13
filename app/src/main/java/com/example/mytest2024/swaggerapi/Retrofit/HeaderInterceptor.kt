package com.example.mytest2024.swaggerapi.Retrofit

import com.example.mytest2024.swaggerapi.LoginUserInformation
import okhttp3.Interceptor
import okhttp3.Response


/* retrofit 에서 header 요청 할때 , deviceId, userSn을 공통으로 보낼려고*/
class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val userSn: String? = LoginUserInformation.userSn
        val deviceId: String? = LoginUserInformation.deviceId


        val url = chain.request().url.toString().contains("v1/user/login")

        if(url){
            return chain.proceed(builder.build())
        }else{
            if (userSn != null && deviceId != null) {
                builder.addHeader("userSn", userSn)
                builder.addHeader("deviceId", deviceId)
            }
        }


        return chain.proceed(builder.build())
    }

}