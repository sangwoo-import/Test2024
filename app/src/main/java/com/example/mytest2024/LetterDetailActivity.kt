package com.example.mytest2024

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mytest2024.swaggerapi.LetterInformation
import com.example.mytest2024.swaggerapi.LoginUserInformation
import com.example.mytest2024.swaggerapi.Retrofit.FileListData
import com.example.mytest2024.swaggerapi.Retrofit.LetterDetailRequestData
import com.example.mytest2024.swaggerapi.swaggercontroller.LetterDetailProvider
import com.example.mytest2024.databinding.LetterDetailActivityBinding


class LetterDetailActivity : AppCompatActivity() {

    /* ViewModel 객체 생성 */


    private val letterViewModel :LetterDetailProvider by lazy {LetterDetailProvider() }

    private lateinit var binding: LetterDetailActivityBinding


    /*response Data */
//    private var codeA: String = ""
//    private var messageA: String = ""
//
//    private var letterTitleA: String = ""
//    private var sendUserNameA: String = ""
//    private var letterContentA: String = ""
//    private var recipientUserA: String = ""
//    private var carbonCopyUserA: String = ""
//
//    private var fileListData: List<FileListData> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = LetterDetailActivityBinding.inflate(layoutInflater)
//        setContentView(binding.root)


        binding = DataBindingUtil.setContentView(this, R.layout.letter_detail_activity)

        with(binding) {
            lifecycleOwner = this@LetterDetailActivity
            viewModel = letterViewModel
        }


        // 통신 타이밍 안맞아서 먼저 보여주고 바뀌니깐

        // 일단 여기들어오면 바로 안보이게 하고 성공과 실패되면 보여주고 안보여주고 할려고
        binding.letterDetailConstraint.visibility = View.GONE


        /* Request Data */

        /*requestData*/
        var userSnA = LoginUserInformation.userSn.toString()
        var letterSnA = LetterInformation.letterSn.toString()
        var passwordA = LetterInformation.password.toString()

        val letterDetailRequestDataSet = LetterDetailRequestData(

            letterSnA, userSnA, passwordA
        )

        letterDetailRequest(letterDetailRequestDataSet)







        letterViewModel.getLetterDetailResponse.observe(this) { data ->

            if (data.resultCode.equals("100")) {
                Toast.makeText(
                    this@LetterDetailActivity,
                    data.resultCode + data.resultMsg,
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("success", data.resultCode + ": " + data.resultMsg)
                // 성공하면 화면 보이게
                binding.letterDetailConstraint.visibility = View.VISIBLE
            } else {
                // 비번 안 맞으면 아예 안보이게
                binding.letterDetailConstraint.visibility = View.GONE
                Log.d("error!!", data.resultCode + ": " + data.resultMsg)
                Toast.makeText(this@LetterDetailActivity, data.resultMsg, Toast.LENGTH_SHORT).show()
            }
        }




    }


    fun letterDetailRequest(letterDetailRequestDataSet: LetterDetailRequestData) {
        letterViewModel.letterDetailGo(letterDetailRequestDataSet)

    }

//    override fun completeLetterDetail(
//        code: String,
//        msg: String,
//        resultData: List<LetterDetailResponse>,
//        fileListData: List<FileListData>
//    ) {
//        codeA = code
//        messageA = msg
//
//        // 성공하면 화면 보이게
//        binding.letterDetailConstraint.visibility = View.VISIBLE
//
//
//
//        if (codeA.equals("100")) {
//            Log.d("success", codeA + ": " + messageA)
//
//
//
//            for (dataItem in resultData) {
//                letterTitleA = dataItem.letterTitle
//                sendUserNameA = dataItem.senderUserName
//                letterContentA = dataItem.letterContent
//
//                // 복습
//                recipientUserA = dataItem.recipientUser.joinToString(",")
//                carbonCopyUserA = dataItem.carbonCopyUser.joinToString(",")
//
//            }
//
//
//            // 텍스트 클릭 활성화
//            binding.filename.movementMethod = LinkMovementMethod.getInstance()
//            val spannableBuilder = SpannableStringBuilder()
//            // 파일 String 값 활성화
//            fileListData.forEachIndexed { index, fileData ->
//                val fileName = fileData.fileName
//                val spannable = SpannableString(fileName)
//
//                spannable.setSpan(object : ClickableSpan() {
//                    override fun onClick(widget: View) {
//                        // 이벤트 처리(TODO 지금은 String만 출력 -> 나중에 클릭했을 때 이벤트 추가하자)
//
//                        widget.setOnClickListener { }
//                    }
//
//                    override fun updateDrawState(ds: TextPaint) {
//                        super.updateDrawState(ds)
//
//                        ds.isUnderlineText = true
//                        ds.color = Color.BLACK
//                        ds.bgColor = Color.TRANSPARENT
//                    }
//                }, 0, fileName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//                spannableBuilder.append(spannable)
//
//                if (index != fileListData.size - 1) {
//                    spannableBuilder.append(", ")
//                }
//
//            }
//
//
//
//
//
//
//            if (letterTitleA.isNullOrEmpty()) {
//                binding.letterTitleTextView.text = "데이터 없음"
//            } else {
//                binding.letterTitleTextView.text = letterTitleA
//            }
//            if (sendUserNameA.isNullOrEmpty()) {
//                binding.sendUser.text = "데이터 없음"
//            } else {
//                binding.sendUser.text = sendUserNameA
//            }
//
//            if (recipientUserA.isNullOrEmpty()) {
//                binding.recipientUser.text = "데이터 없음"
//            } else {
//                binding.recipientUser.text = recipientUserA
//            }
//            if (carbonCopyUserA.isNullOrEmpty()) {
//                binding.carbonCopyUser.text = "데이터 없음"
//            } else {
//                binding.carbonCopyUser.text = carbonCopyUserA
//            }
//            if (letterContentA.isNullOrEmpty()) {
//                binding.letterContent.text = "내용 없음"
//            } else {
//                binding.letterContent.text = HtmlCompat.fromHtml(
//                    letterContentA,
//                    HtmlCompat.FROM_HTML_MODE_LEGACY
//                )
//            }
//            // 파일
//            if (spannableBuilder.isNullOrEmpty()) {
//                binding.filename.text = "내용 없음"
//            } else {
//                binding.filename.text = spannableBuilder
//
//            }
//
//
//        } else {
//            // 비번 안 맞으면 아예 안보이게
//            binding.letterDetailConstraint.visibility = View.GONE
//            Log.d("error!!", codeA + ": " + messageA)
//            Toast.makeText(this@LetterDetailActivity, messageA, Toast.LENGTH_SHORT).show()
//
//        }
//
//    }


}