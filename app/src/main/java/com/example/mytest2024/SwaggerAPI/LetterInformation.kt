package com.example.mytest2024.SwaggerAPI


/* 편지 상세 조회할 때 사용 */
object LetterInformation {

    var letterSn: String? = null
    var password: String? = null

    fun saveLetterInfo(letterSn: String?) {
        this.letterSn = letterSn
    }

    fun savePassWord(password: String?) {
        this.password = password
    }


}