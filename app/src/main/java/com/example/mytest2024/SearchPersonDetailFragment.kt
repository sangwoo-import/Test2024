package com.example.mytest2024

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.mytest2024.swaggerapi.swaggercontroller.SearchPersonDetailProvider
import com.example.mytest2024.swaggerapi.Retrofit.SearchPersonDetail
import com.example.mytest2024.swaggerapi.Retrofit.SearchPersonDetailRequestData
import com.example.mytest2024.swaggerapi.SearchUserInformation
import com.example.mytest2024.swaggerapi.SearchUserInformation.userSn
import com.example.mytest2024.databinding.SearchpersonDetailFragmentBinding

class SearchPersonDetailFragment : DialogFragment(), SearchPersonDetailProvider.CallBack {

    private val searchPersonDetailProvider =
        SearchPersonDetailProvider(this@SearchPersonDetailFragment)


    /*response Data 및  싱글 톤 저장*/
    private var codeA: String = ""
    private var messageA: String = ""

    var userNameA: String = ""
    var upWardNameA: String = ""
    var wardNmA: String = ""
    var classNmA: String = ""
    var mobilePhoneNumA: String = ""
    var companyTelNumA: String = ""
    var emailA: String = ""
    var userProfileImageA: String = ""


    private lateinit var binding: SearchpersonDetailFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = SearchpersonDetailFragmentBinding.inflate(layoutInflater)


        binding.searchPersonDetailConstraint.visibility = View.GONE

        /*request Data*/
        val userSnA = userSn.toString()
        val SearchPersonDetailRequestDataSet = SearchPersonDetailRequestData(
            userSnA
        )

        requestSearchPersonDetail(SearchPersonDetailRequestDataSet)






        return binding.root // root는 contraint 전체를 뜻한다
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        // 배경 투명 색으로 하기
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.setCanceledOnTouchOutside(true)

        binding.dialogFragmentDisMissBtn.setOnClickListener {
           dialog?.dismiss()
        }
    }


    fun requestSearchPersonDetail(searchPersonDetailRequestDataSet: SearchPersonDetailRequestData) {
        searchPersonDetailProvider.SearchPersonDetailGo(searchPersonDetailRequestDataSet)
    }

    override fun completeSearchPersonDetail(
        code: String,
        msg: String,
        resultData: List<SearchPersonDetail>
    ) {

        codeA = code
        messageA = msg

        if (codeA.equals("100")) {

            binding.searchPersonDetailConstraint.visibility = View.VISIBLE



            for (dataItem in resultData) {
                userNameA = dataItem.userName
                upWardNameA = dataItem.upWardNm
                wardNmA = dataItem.wardNm
                classNmA = dataItem.classNm
                mobilePhoneNumA = dataItem.mobileTelNo
                companyTelNumA = dataItem.companyTelNo
                emailA = dataItem.email
                userProfileImageA = dataItem.userProfileImage
            }
            // 직원 상세 정보 저장
            SearchUserInformation.saveSearchUserDetailInfo(
                companyTelNumA,
                emailA,
                userProfileImageA
            )


            val image = "https://newsimg.sedaily.com/2021/12/09/22V85NTJGY_1.jpg"
            if (userProfileImageA.isNullOrEmpty()) {
                binding.userProfileImageView.visibility = View.VISIBLE
            } else {
                Glide.with(this@SearchPersonDetailFragment)
                    .load(userProfileImageA) // userProfileImageA 값
                    .into(binding.userProfileImageView)
            }

            /*데이터 없을 때 표시 */

            if (userNameA.isNullOrEmpty()) {
                binding.searchUsername.text = "데이터없음"

            } else {
                binding.searchUsername.text = userNameA
            }
            if (upWardNameA.isNullOrEmpty()) {
                binding.searchupWardName.text = "데이터없음"
            } else {
                binding.searchupWardName.text = upWardNameA
            }
            if (wardNmA.isNullOrEmpty()) {
                binding.searchWardName.text = "데이터없음"
            } else {
                binding.searchWardName.text = wardNmA
            }
            if (classNmA.isNullOrEmpty()) {
                binding.searchClassName.text = "데이터없음"
            } else {
                binding.searchClassName.text = classNmA
            }
            if (mobilePhoneNumA.isNullOrEmpty()) {
                binding.MobilePhoneNum.text = "데이터없음"
            } else {
                /* 밑줄 및 색상 파란색 변경*/
                val spannableString = SpannableString(mobilePhoneNumA).apply {
                    setSpan(UnderlineSpan(), 0, length, 0)
                    setSpan(ForegroundColorSpan(Color.BLUE), 0, length, 0)
                }

                binding.MobilePhoneNum.text = spannableString


                /* 다이얼로 가기 기능 */
                binding.MobilePhoneNum.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + mobilePhoneNumA))
                    intent.apply { startActivity(intent) }
                }
            }

            if (companyTelNumA.isNullOrEmpty()) {
                binding.CompanyPhoneNum.text = "데이터없음"
            } else {
                /* 밑줄 및 색상 파란색 변경*/
                val spannableString = SpannableString(companyTelNumA).apply {
                    setSpan(UnderlineSpan(), 0, length, 0)
                    setSpan(ForegroundColorSpan(Color.BLUE), 0, length, 0)
                }
                binding.CompanyPhoneNum.text = spannableString

                binding.CompanyPhoneNum.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + companyTelNumA))
                    intent.apply { startActivity(intent) }
                }
            }
            if (emailA.isNullOrEmpty()) {
                binding.email.text = "데이터없음"
            } else {
                binding.email.text = emailA
                binding.email.linksClickable = true
            }


            /* 오류 분기 처리*/
        } else if (codeA.equals("E100")) {
            binding.searchPersonDetailConstraint.visibility = View.GONE

            Log.d("error!!", codeA + ": " + messageA)
            Toast.makeText(requireContext(), messageA, Toast.LENGTH_SHORT).show()

        } else if (codeA.equals("E600")) {
            binding.searchPersonDetailConstraint.visibility = View.GONE

            Log.d("error!!", codeA + ": " + messageA)
            Toast.makeText(requireContext(), messageA, Toast.LENGTH_SHORT).show()
        } else {
            binding.searchPersonDetailConstraint.visibility = View.GONE

            Log.d("error!!", codeA + ": " + messageA)
            Toast.makeText(requireContext(), messageA, Toast.LENGTH_SHORT).show()

        }

    }

}