package com.example.mytest2024.SwaggerAPI.Retrofit


import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST


interface RetrofitService {


    // Headers 에 권한 부여
    /* 로그인 */
    @Headers("accept: */*", "Content-Type: application/json")
    @POST("v1/user/login")  // Request body 역할
    fun sendLogin(@Body loginRequestStart: LoginRequestData): retrofit2.Call<CommonResponseData<List<LoginResponse>>>


    /* 로그아웃 */
    @Headers(
        "accept: */*",
        "Content-Type: application/json",
    )
    @POST("v1/user/logout")
    fun sendLogout(@Body logoutRequestStart: LogoutRequestData): retrofit2.Call<LogoutResponseData>

    /* 직원 목록 조회 */
    @Headers(
        "accept: */*",
        "Content-Type: application/json",
    )
    @POST("v1/staff/list")
    fun searchPerson(@Body searchPersonStart: SearchPersonRequestData): retrofit2.Call<CommonResponseData<List<SearchPerson>>>

    /* 직원 목록 상세 조회 */
    @Headers(
        "accept: */*",
        "Content-Type: application/json",
    )
    @POST("v1/staff/info")
    fun searchPersonDetail(@Body searchPersonDetail: SearchPersonDetailRequestData): retrofit2.Call<CommonResponseData<List<SearchPersonDetail>>>

    /* 자유 토론 방 목록 조회 */
    @Headers(
        "accept: */*",
        "Content-Type: application/json",
    )
    @POST("v1/board/free-discussion/list")
    fun freeTalk(@Body freeTalkContent: FreeTalkRequestData): retrofit2.Call<CommonResponseData<List<FreeTalkResponse>>>


    /* 경조사 목록 조회*/
    @Headers(
        "accept: */*",
        "Content-Type: application/json",
    )
    @POST("v1/board/family-event/list")
    fun familyEvent(@Body familyEventTalkContent: FamilyEventRequestData): retrofit2.Call<CommonResponseData<List<FamilyEventResponse>>>


    /* 자유 토론방 및 경조사 -> 게시글 상세 조회 */
    @Headers(
        "accept: */*",
        "Content-Type: application/json",
    )
    @POST("v1/board/detail")

    fun boardDetailContent(@Body boardDetailContent: BoardDetailRequestData): retrofit2.Call<CommonResponseData<List<BoardDetailResponse>>>


    @Headers(
        "accept: */*",
        "Content-Type: application/json",
    )

    /* 편지 목록 조회 */
    @POST("v1/letter/list")
    fun letterContent(@Body letterContent: LetterRequestData): retrofit2.Call<CommonResponseData<List<LetterResponse>>>

    @Headers(
        "accept: */*",
        "Content-Type: application/json",
    )

    @POST("v1/letter/detail")
    fun letterDetailContent(@Body letterDetailContent: LetterDetailRequestData): retrofit2.Call<CommonResponseData<List<LetterDetailResponse>>>


    /* 설문조사 목록 조회 */
    @Headers(
        "accept: */*",
        "Content-Type: application/json",
    )
    @POST("v1/survey/list")
    fun surveyContent(@Body surveyContent: SurveyRequestData): retrofit2.Call<CommonResponseData<List<SurveyResponse>>>


    /* 설문조사 상세 조회 */
    @Headers(
        "accept: */*",
        "Content-Type: application/json",
    )
    @POST("v1/survey/detail")
    fun surveyDetailContent(@Body surveyDetailContent: SurveyDetailRequestData): retrofit2.Call<CommonResponseData<List<SurveyDetailResponse>>>

/*설문 조사 답변 저장 */
    @Headers(
        "accept: */*",
        "Content-Type: application/json",
    )
    @POST("v1/survey/answer")
    fun surveySaveAnswerContent(@Body surveySaveAnswerContent : SurveyAnswerSaveRequestData): retrofit2.Call<SurveySaveAnswerResponse>
}
