package com.example.mytest2024

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import coil.memory.MemoryCache
import com.example.mytest2024.RecyclerView.SurveyDetailRecyclerViewAdapter
import com.example.mytest2024.SwaggerAPI.LoginUserInformation

import com.example.mytest2024.SwaggerAPI.Retrofit.SurveyAnswerSaveRequestData
import com.example.mytest2024.SwaggerAPI.Retrofit.SurveyDetailRequestData
import com.example.mytest2024.SwaggerAPI.Retrofit.SurveyDetailResponse
import com.example.mytest2024.SwaggerAPI.Retrofit.SurveyQuestionList
import com.example.mytest2024.SwaggerAPI.Retrofit.SurveyRequestData
import com.example.mytest2024.SwaggerAPI.SurveyInformation
import com.example.mytest2024.SwaggerAPI.SwaggerController.BoardDetailProvider
import com.example.mytest2024.SwaggerAPI.SwaggerController.SurveyDetailProvider
import com.example.mytest2024.SwaggerAPI.SwaggerController.SurveySaveAnswerProvider
import com.example.mytest2024.databinding.ActivityBoardDetailBinding
import com.example.mytest2024.databinding.ActivitySurveyDetailBinding
import okhttp3.internal.userAgent

class SurveyDetailActivity : AppCompatActivity(), SurveyDetailProvider.CallBack,
    SurveySaveAnswerProvider.CallBack {

    private lateinit var binding: ActivitySurveyDetailBinding

    /* 설문 조사 상세 조회 */
    private val surveyDetailProvider = SurveyDetailProvider(this@SurveyDetailActivity)

    /* 설문 조사 답변 저장 */
    private val surveySaveAnswerProvider = SurveySaveAnswerProvider(this@SurveyDetailActivity)


    private lateinit var adapter: SurveyDetailRecyclerViewAdapter


    /*Response data */
    private var codeA: String = ""
    private var messageA: String = ""
    private var surveyTitleA: String = ""
    private var beginDateA: String = ""
    private var endDateA: String = ""
    var answerAtA: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 통신 타이밍 안맞아서 먼저 보여주고 바뀌니깐
        // 일단 여기들어오면 바로 안보이게 하고 성공과 실패되면 보여주고 안보여주고 할려고
        binding.surveyDetailScrollView.visibility = View.GONE


        /*request Data*/
        val userSn = LoginUserInformation.userSn
        val surveySn = SurveyInformation.surveySn


        /* 들어 가자 마자 실행 */
        val surveyDetailRequestDataSet = SurveyDetailRequestData(
            userSn.toString(), surveySn.toString()
        )
        requestSurveyDetail(surveyDetailRequestDataSet)

        binding.progressBarLinearLayout.visibility = View.VISIBLE


        /* 다시  들어 가면 답변 저장하기 위한 list 초기화 시키기*/
        SurveyInformation.clearAllList()


        /*답변 저장을 requestData*/
        /*버튼을 누르면 요청*/
        binding.surveySaveAnswerBtn.setOnClickListener {
            /*답변 저장 Request Data */
            var surveyAnswerList = SurveyInformation.allList?.toMutableList()
            val surveySaveAnswerRequestDataSet = SurveyAnswerSaveRequestData(

                surveySn.toString(), userSn.toString(), surveyAnswerList!!
            )

            requestSurveySaveAnswer(surveySaveAnswerRequestDataSet)


        }


    }

    /*설문 조사 상세 조회 요청*/
    private fun requestSurveyDetail(surveyDetailRequestDataSet: SurveyDetailRequestData) {
        surveyDetailProvider.surveyDetailGo(surveyDetailRequestDataSet)
    }


    /* 설문조사 상세 조회 성공 여부 */
    override fun completeSurveyDetail(
        code: String,
        msg: String,
        resultData: List<SurveyDetailResponse>,
        surveyQuestionList: List<SurveyQuestionList>
    ) {

        codeA = code
        messageA = msg
        binding.surveyDetailScrollView.visibility = View.VISIBLE
        binding.progressBarLinearLayout.visibility = View.GONE

        if (codeA.equals("100")) {

            for (item in resultData) {
                surveyTitleA = item.surveyTitle
                beginDateA = item.beginDate
                endDateA = item.endDate
                answerAtA = item.answerAt
            }

            //  답변 여부를 위해 여기선 response 성공시 recyclerview 생성 -> adapter 붙이기
            val recyclerView = binding.surveyDetailRecyclerView
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = LinearLayoutManager(this@SurveyDetailActivity)

            adapter = SurveyDetailRecyclerViewAdapter(
                mutableListOf(),
                this@SurveyDetailActivity,
                answerAtA
            )
            recyclerView.adapter = adapter


            /*답변이 완료 된거는 이미 완료 및 다른 기능들 비활 성화 시키기  */
            if (answerAtA.equals("Y")) {
                binding.surveySaveAnswerBtn.isEnabled = false
                Toast.makeText(this@SurveyDetailActivity, "이미 답변이 완료된 설문 조사입니다.",
                    Toast.LENGTH_SHORT
                ).show()

            }



            if (surveyTitleA.isNullOrEmpty()) {
                binding.SurveyTitleTextView.text = "설문조사 데이터없음."
            } else {
                binding.SurveyTitleTextView.text = surveyTitleA
            }
            if (beginDateA.isNullOrEmpty()) {
                binding.beginDateTextView.text = "시작날짜없음"
            } else {
                binding.beginDateTextView.text = beginDateA
            }
            if (endDateA.isNullOrEmpty()) {
                binding.endDateTextView.text = "응시종료날짜없음"
            } else {
                binding.endDateTextView.text = endDateA
            }

            if (surveyQuestionList.isNullOrEmpty()) {
                binding.emptySurvey.visibility = View.VISIBLE
                binding.surveyDetailRecyclerView.visibility = View.GONE
            } else {
                // 데이터가 있으면 어댑터에 추가  하고 Recyclerview 출력 및 댓글 없어요 텍스트 가리기
                adapter.addData(surveyQuestionList)

                binding.surveyDetailRecyclerView.visibility = View.VISIBLE
                binding.emptySurvey.visibility = View.GONE

            }


        } else {

            binding.surveyDetailScrollView.visibility = View.GONE
            Log.d("error!!", codeA + ": " + messageA)
            Toast.makeText(this@SurveyDetailActivity, messageA, Toast.LENGTH_SHORT).show()
        }

    }


    /* 설문 조사 답변 저장 요청*/

    private fun requestSurveySaveAnswer(surveySaveAnswerRequestDataSet: SurveyAnswerSaveRequestData) {

        surveySaveAnswerProvider.surveySaveAnswerGo(surveySaveAnswerRequestDataSet)
    }


    /*설문 조사 답변 저장 성공 여부*/
    override fun completeSurveySaveAnswer(code: String, msg: String) {
        codeA = code
        messageA = msg


        if (codeA.equals("100")) {
            Toast.makeText(this@SurveyDetailActivity, messageA, Toast.LENGTH_SHORT).show()

            // 저장 되면 밖으로 나가기
            val intent = Intent(this@SurveyDetailActivity, SurveyActivity::class.java)
            intent.apply {
                startActivity(intent)
                finish()
            }

        } else {
            Toast.makeText(this@SurveyDetailActivity, messageA, Toast.LENGTH_SHORT).show()
        }

    }


}