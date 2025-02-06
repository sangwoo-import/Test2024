package com.example.mytest2024.SwaggerAPI


/* 싱글 톤 패턴으로 사용자 정보 담기 */
object SearchUserInformation {
    var userSn: String? = null
    var userName: String? = null
    var upWardNm: String? = null
    var wardName: String? = null
    var className: String? = null
    var mobileTelNo: String? = null
    var companyTelNo: String? = null
    var email: String? = null
    var userProfileImage: String? = null


    /* 직원 목록 조회 -> 직원 상세 조회 해서 보여줄 데이터와 직원 상세 조회에 날려야할 request 데이터 저장
     */
    fun saveSearchUserInfo(
        userSn: String?, userName: String?, upWardNm: String?,
        wardNm: String?, classNm: String?, mobileTelNum: String?
    ) {
        this.userSn = userSn
        this.userName = userName
        this.upWardNm = upWardNm
        this.wardName = wardNm
        this.className = classNm
        this.mobileTelNo = mobileTelNum
    }


    /* 직원 목록 상세 조회  회사 번호랑 , 이메일, 이미지만 */
    fun saveSearchUserDetailInfo(
        companyTel: String?, email: String?, userProfileImage: String?
    ) {
        this.companyTelNo = companyTel
        this.email = email
        this.userProfileImage = userProfileImage
    }


}