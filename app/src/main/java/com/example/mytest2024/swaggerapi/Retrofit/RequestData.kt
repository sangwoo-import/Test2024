package com.example.mytest2024.swaggerapi.Retrofit

import com.google.gson.annotations.SerializedName

/* 로그인 */
data class LoginRequestData(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("userPw")
    val userPw: String,
    @SerializedName("osVersionName")
    val osVersionName: String,
    @SerializedName("deviceName")
    val deviceName: String,
    @SerializedName("fcmToken")
    val fcmToken: String,
    @SerializedName("deviceId")
    val deviceId: String
)

/* 로그아웃 */
data class LogoutRequestData(

    @SerializedName("userSn")
    val userSn: String

)

/* 직원 목록 */
data class SearchPersonRequestData(

    @SerializedName("keyword")
    val keyword: String,
    @SerializedName("searchType")
    val searchType: String,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("lastRowNum")
    val lastRowNum: Int,
)

/*직원 상세 목록 조회*/
data class SearchPersonDetailRequestData(

    @SerializedName("userSn")
    val userSn: String
)

/* 자유 토론방 */
data class FreeTalkRequestData(
    @SerializedName("keyword")
    val keyword: String,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("lastRowNum")
    val lastRowNum: Int
)

/* 자유 토론방 및 게시글 상세 조회 */

data class BoardDetailRequestData(
    @SerializedName("bbsId")
    val bbsId: String,
    @SerializedName("postingSn")
    val postingSn: Int
)

/*경조사 */
data class FamilyEventRequestData(
    @SerializedName("keyword")
    val keyword: String,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("lastRowNum")
    val lastRowNum: Int
)

/*경조사 상세 조회 및 게시글 상세 조회*/
data class FamilyEventDetailRequestData(
    @SerializedName("bbsId")
    val bbsId: String,
    @SerializedName("postingSn")
    val postingSn: Int
)


/* 편지 목록 조회 */
data class LetterRequestData(

    @SerializedName("userSn")
    val userSn: String,
    @SerializedName("keyword")
    var keyword: String,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("lastRowNum")
    val lastRowNum: Int
)

/*편지 상세 조회 */
data class LetterDetailRequestData(

    @SerializedName("letterSn")
    val letterSn: String,
    @SerializedName("userSn")
    val userSn: String,
    @SerializedName("password")
    val password: String
)


/* 설문 조사 목록 조회 */

data class SurveyRequestData(
    @SerializedName("userSn")
    val userSn: String,
    @SerializedName("keyword")
    val keyword: String,
    @SerializedName("pageSize")
    val pageSize: Int,
    @SerializedName("lastRowNum")
    val lastRowNum: Int
)


/* 설문 조사 상세 조회 */

data class SurveyDetailRequestData(
    @SerializedName("userSn")
    val userSn: String,
    @SerializedName("surveySn")
    val surveySn: String
)

/* 설문 조사 답변 저장 */

data class SurveyAnswerSaveRequestData(
    @SerializedName("surveySn")
    val surveySn: String,
    @SerializedName("userSn")
    val userSn: String,
    @SerializedName("questionList")
    val questionList: MutableList<SurveyAnswerSaveQuestionRequestData>
)

data class SurveyAnswerSaveQuestionRequestData(
    @SerializedName("surveyQuestionSn")
    val surveyQuestionSn: String,
    @SerializedName("questionOptionSn")
    val questionOptionSn: String,
    @SerializedName("etcAnswerContent")
    val etcAnswerContent: String? = null
)

