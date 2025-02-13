package com.example.mytest2024.swaggerapi.Retrofit

import com.google.gson.annotations.SerializedName

data class CommonResponseData<T>(
    // T -> 타입   List<LoginResponse> 를 넣는다고 생각하면됨
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("resultMsg")
    val resultMsg: String,  // 필요  성공과 실패 또는 오류 문구일때
    @SerializedName("resultSize")
    val resultSize: Int,
    @SerializedName("totalSize")
    val totalSize: Int,
    @SerializedName("resultData")
    val resultData: T, //필요
)

data class LoginResponse(
    @SerializedName("userSn")
    val userSn: String,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("userName")
    val userName: String, // my로 가야함
    @SerializedName("wardId")
    val wardId: String,
    @SerializedName("wardNm")
    val wardNm: String,  // my로 가야함
    @SerializedName("upWardId")
    val upWardId: String,
    @SerializedName("upWardNm")
    val upWardNm: String,
    @SerializedName("classCd")
    val classCd: String,
    @SerializedName("classNm")
    val classNm: String // my로 가야함
)

data class LogoutResponseData(

    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("resultMsg")
    val resultMsg: String,  // 필요  성공과 실패 또는 오류 문구일때
)

/* 직원 검색 */
data class SearchPerson(
    @SerializedName("userSn")
    val userSn: String,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("upWardNm")
    val upWardNm: String,
    @SerializedName("wardNm")
    val wardNm: String,
    @SerializedName("classNm")
    val classNm: String,
    @SerializedName("mobileTelNo")
    val mobileTelNo: String,
)

/* 직원 상세 검색 */
data class SearchPersonDetail(
    @SerializedName("userSn")
    val userSn: String,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("upWardNm")
    val upWardNm: String,
    @SerializedName("wardNm")
    val wardNm: String,
    @SerializedName("classNm")
    val classNm: String,
    @SerializedName("mobileTelNo")
    val mobileTelNo: String,
    @SerializedName("companyTelNo")
    val companyTelNo: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("userProfileImage")
    val userProfileImage: String
)

/*  자유 토론방 목록 조회 */

data class FreeTalkResponse(
    @SerializedName("rnum")
    val rnum: Int,
    @SerializedName("boardId")
    val boardId: String,
    @SerializedName("postingSn")
    val postingSn: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("writerName")
    val writerName: String? = null,
    @SerializedName("registerDate")
    val registerData: String,
    @SerializedName("noticeAt")
    val noticeAt: String,
    @SerializedName("totalCount")
    val totalCount: Int,
)

/*경조사 */
data class FamilyEventResponse(
    @SerializedName("rnum")
    val rnum: Int,
    @SerializedName("boardId")
    val boardId: String,
    @SerializedName("postingSn")
    val postingSn: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("writerName")
    val writerName: String? = null,
    @SerializedName("registerDate")
    val registerData: String,
    @SerializedName("noticeAt")
    val noticeAt: String,
    @SerializedName("totalCount")
    val totalCount: Int,
)

/* 자유 토론방 및 경조사 ->  게시글 상세 조회 */


data class BoardDetailResponse(
    @SerializedName("boardId")
    val boardId: String,
    @SerializedName("postingSn")
    val postingSn: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("writerName")
    val writerName: String? = null,
    @SerializedName("registerDate")
    val registerDate: String? = null,
    @SerializedName("postingBeginDate")
    val postingBeginDate: String,
    @SerializedName("postingEndDate")
    val postingEndDate: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("postingCategorySn")
    val postingCategorySn: String? = null,
    @SerializedName("postingCategoryNm")
    val postingCategoryNm: String? = null,
    @SerializedName("emtcnLike")
    val emtcnLike: Int,
    @SerializedName("emtcnSad")
    val emtcnSad: Int,
    @SerializedName("emtcnLove")
    val emtcnLove: Int,
    @SerializedName("emtcnBest")
    val emtcnBest: Int,
    @SerializedName("emtcnBestAnalysis")
    val emtcnBestAnalysis: Int,
    @SerializedName("emtcnFollowUp")
    val emtcnFollowUp: Int,
    @SerializedName("atchAt")
    val atchAt: String,
    @SerializedName("boardFileList")
    val boardFileList: List<BoardFileListData>,
    @SerializedName("boardCommentList")
    val boardCommentList: List<BoardCommentListData>

)

/* 자유 토론방 및 경조사 -> 게시글 상세 조회 File List */

data class BoardFileListData(
    @SerializedName("fileSn")
    val fileSn: Int,
    @SerializedName("storeFileNm")
    val storeFileNm: String,
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("fileSize")
    val fileSize: Int,
    @SerializedName("fileUrl")
    val fileUrl: String? = null
)

/* 자유 토론방 및 경조사 ->게시글 상세 조회 댓글 리스트 */
data class BoardCommentListData(
    @SerializedName("commentSn")
    val commentSn: Int,
    @SerializedName("writerName")
    val writerName: String,
    @SerializedName("commentContent")
    val commentContent: String,
    @SerializedName("deleteAt")
    val deleteAt: String,
    @SerializedName("registerDate")
    val registerDate: String,
    @SerializedName("upCommentSn")
    val upCommentSn: Int,
    @SerializedName("commentDepth")
    val commentDepth: Int,
    @SerializedName("likeCount")
    val likeCount: Int,
    @SerializedName("dislikeCount")
    val dislikeCount: Int,
    @SerializedName("childCommentList")
    val childCommentList: List<ChildResponseData>
)

/*  자식 댓글 == 대댓글 까지 */

data class ChildResponseData(
    @SerializedName("commentSn")
    val commentSn: Int,
    @SerializedName("writerName")
    val writerName: String,
    @SerializedName("commentContent")
    val commentContent: String,
    @SerializedName("registerDate")
    val registerDate: String,
    @SerializedName("likeCount")
    val likeCount: Int,
    @SerializedName("dislikeCount")
    val dislikeCount: Int,
    //@SerializedName("childCommentList")
    //val childCommentList: List<ChildResponseData>

)


/* 편지 목록 조회*/

data class LetterResponse(
    @SerializedName("rnum")
    val rnum: Int,
    @SerializedName("letterSn")
    val letterSn: String,
    @SerializedName("letterTitle")
    val letterTitle: String,
    @SerializedName("senderUserSn")
    val senderUserSn: String,
    @SerializedName("senderUserName")
    val senderUserName: String,
    @SerializedName("sendDate")
    val sendDate: String,
    @SerializedName("receiveAt")
    val receiveAt: String,
    @SerializedName("passwordAt")
    val passwordAt: String,
    @SerializedName("letterBoxSn")
    val letterBoxSn: String? = null,
    @SerializedName("letterBoxName")
    val letterBoxName: String? = null,
    @SerializedName("userProfileImage")
    val userProfileImage: String,
    @SerializedName("totalCount")
    val totalCount: Int
)

/* 편지 상세 목록 조회 */

data class LetterDetailResponse(
    @SerializedName("letterSn")
    val letterSn: String,
    @SerializedName("letterTitle")
    val letterTitle: String,
    @SerializedName("senderUserSn")
    val senderUserSn: String,
    @SerializedName("senderUserName")
    val senderUserName: String,
    @SerializedName("letterContent")
    val letterContent: String,
    @SerializedName("recipientUser")
    val recipientUser: List<String>,
    @SerializedName("carbonCopyUser")
    val carbonCopyUser: List<String>,
    @SerializedName("fileList")
    val fileList: List<FileListData>

)

data class FileListData(
    @SerializedName("fileSn")
    val fileSn: String,
    @SerializedName("fileName")
    val fileName: String,
    @SerializedName("fileSize")
    val fileSize: String
)


/* 설문 조사 목록 조회 */

data class SurveyResponse(
    @SerializedName("rnum")
    val rnum: Int,
    @SerializedName("surveySn")
    val surveySn: String,
    @SerializedName("surveyTitle")
    val surveyTitle: String,
    @SerializedName("questionCount")
    val questionCount: Int,
    @SerializedName("beginDate")
    val beginDate: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("surveyTypeCd")
    val surveyTypeCd: String,
    @SerializedName("resultsRevealAt")
    val resultsRevealAt: String,
    @SerializedName("answerAt")
    val answerAt: String,
    @SerializedName("beginAt")
    val beginAt: String,
    @SerializedName("totalCount")
    val totalCount: Int
)

/* 설문 조사 상세 조회 */

data class SurveyDetailResponse(
    @SerializedName("surveySn")
    val surveySn: String,
    @SerializedName("surveyTitle")
    val surveyTitle: String,
    @SerializedName("questionCount")
    val questionCount: Int,
    @SerializedName("beginDate")
    val beginDate: String,
    @SerializedName("endDate")
    val endDate: String,
    @SerializedName("surveyTypeCd")
    val surveyTypeCd: String,
    @SerializedName("surveyTypeNm")
    val surveyTypeNm: String,
    @SerializedName("resultsRevealAt")
    val resultsRevealAt: String,
    @SerializedName("answerAt")
    val answerAt: String,
    @SerializedName("beginAt")
    val beginAt: String,
    @SerializedName("questionList")
    val questionList: List<SurveyQuestionList>

)

/* 설문 조사 상세 조회   문항들  리스트 */

data class SurveyQuestionList(
    @SerializedName("surveySn")
    val surveySn: String,
    @SerializedName("surveyQuestionSn")
    val surveyQuestionSn: String,
    @SerializedName("questionNm")
    val questionNm: String,
    @SerializedName("questionSeCd")
    val questionSeCd: String,
    @SerializedName("questionSeCdNm")
    val questionSeCdNm: String,
    @SerializedName("multiCheckAt")
    val multiCheckAt: String,
    @SerializedName("answerItemCount")
    val answerItemCount: Int,
    @SerializedName("etcAnswerAt")
    val etcAnswerAt: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("imageFileName")
    val imageFileName: String,
    @SerializedName("answerUserCount")
    val answerUserCount: Int? = null,
    @SerializedName("optionList")
    val optionList: List<SurveyOptionListResponse>
)

data class SurveyOptionListResponse(

    @SerializedName("surveySn")
    val surveySn: String,
    @SerializedName("surveyQuestionSn")
    val surveyQuestionSn: String?= null,
    @SerializedName("questionOptionSn")
    val questionOptionSn: String?= null,
    @SerializedName("quarterQuestionSn")
    val quarterQuestionSn: String?= null,
    @SerializedName("questionOptionContent")
    val questionOptionContent: String?= null,
    @SerializedName("answerAt")
    val answerAt: String?= null,
    @SerializedName("etcAnswerContent")
    var etcAnswerContent: String? = null,
    @SerializedName("answerCount")
    val answerCount: Int? = null,
    @SerializedName("totalAnswerCount")
    val totalAnswerCount: Int? = null,
    @SerializedName("answerStats")
    val answerStats: Int? = null,
    var isChecked : Boolean = false

)

/* 설문 조사 답변 저장 */
data class SurveySaveAnswerResponse(
    @SerializedName("resultCode")
    val resultCode: String,
    @SerializedName("resultMsg")
    val resultMsg: String,
)






