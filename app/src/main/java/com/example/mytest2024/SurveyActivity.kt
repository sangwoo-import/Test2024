package com.example.mytest2024

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest2024.RecyclerView.LetterRecyclerViewAdapter
import com.example.mytest2024.RecyclerView.SurveyRecyclerViewAdapter
import com.example.mytest2024.SwaggerAPI.LetterInformation
import com.example.mytest2024.SwaggerAPI.LoginUserInformation
import com.example.mytest2024.SwaggerAPI.Retrofit.LetterRequestData
import com.example.mytest2024.SwaggerAPI.Retrofit.SurveyRequestData
import com.example.mytest2024.SwaggerAPI.Retrofit.SurveyResponse
import com.example.mytest2024.SwaggerAPI.SurveyInformation
import com.example.mytest2024.SwaggerAPI.SwaggerController.LetterProvider
import com.example.mytest2024.SwaggerAPI.SwaggerController.SurveyProvider
import com.example.mytest2024.databinding.ActivityLetterBinding
import com.example.mytest2024.databinding.ActivitySurveyBinding

class SurveyActivity : AppCompatActivity(), SurveyProvider.CallBack {
    private lateinit var binding: ActivitySurveyBinding
    private lateinit var adapter: SurveyRecyclerViewAdapter

    private val surveyProvider = SurveyProvider(this@SurveyActivity)

    /*request Data*/
    private val userSn = LoginUserInformation.userSn.toString()
    private var keyword = ""
    private val pageSize = 50
    private var lastRowNUm = 0

    /* Response 상세 조회에 넣을 값 */
    var surveySnA = ""

    /*Response data */
    private var codeA: String = ""
    private var messageA: String = ""

    /* 현재  recyclerView Loading 상태 */
    private var isLoading = false

    /* 더이상 데이터가 없거나 데이터가 없을때 스크롤 방지*/
    private var hasMoreData = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  먼저 제일 먼저 생성
        val recyclerView = binding.surveyRecyclerView
        recyclerView.setHasFixedSize(true)
        // LinearLayoutManager가 RecyclerViewActivity를 통제한다는 의미    LinearLayout 속성에 수직으로 정했으니 수직으로 스크롤 한다.
        recyclerView.layoutManager = LinearLayoutManager(this@SurveyActivity)
        // adapter 붙어주기에 만든 data를 붙여주기 + context
        adapter = SurveyRecyclerViewAdapter(mutableListOf(), this@SurveyActivity)
        recyclerView.adapter = adapter


        /* 들어 가자 마자 실행 */
        val surveyRequestDataSet = SurveyRequestData(

            userSn, keyword, pageSize,
            lastRowNUm
        )
        requestSurvey(surveyRequestDataSet)


        binding.progressBarLinearLayout.visibility = View.VISIBLE


        /*검색창 EditText에 글자 입력 받으면 글자 삭제 버튼 보이고 없으면 삭제 버튼 안보이기 */
        binding.surveyTitleSearchEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.surveyCancelBtn.visibility = View.VISIBLE
                } else {
                    binding.surveyCancelBtn.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}

        }
        )





        /* EditText 적힌거 다 삭제 */
        binding.surveyCancelBtn.setOnClickListener {
            binding.surveyTitleSearchEditText.text.clear()
        }

        /* 제목 검색할 때 */

        binding.surveySearchBtn.setOnClickListener {
            lastRowNUm = 0

            // 어댑터 초기화
            adapter.reset()
            val surveyRequestData2 = SurveyRequestData(
                userSn,
                binding.surveyTitleSearchEditText.text.toString(),
                pageSize,
                lastRowNUm

            )
            requestSurvey(surveyRequestData2)
        }


        /* Floating button 구현 */
        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 1500 }
        val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 1500 }


        /* 스크롤 내렸을 때 */
        binding.surveyRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            var isScrolling = false // 스크롤 초기 상태


            // 스크롤 처음 시작  및 움직 일때  버튼 숨키기
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 아예 젤 처음도 안 보이게
                binding.upFloatingBtn.visibility = View.GONE

                // dy -> 수직 스크롤
                if (dy != 0) {
                    binding.upFloatingBtn.visibility = View.GONE
                }

            }

            // 스크롤 이 처음 이후 이벤트를 조작할 때 사용할때 이용
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        // 젤 상단일 때는 안보이기
                        if (!recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                            binding.upFloatingBtn.startAnimation(fadeOut)
                            binding.upFloatingBtn.visibility = View.GONE
                            isScrolling = true


                        } else {
                            // 멈출때  버튼 보이기
                            binding.upFloatingBtn.startAnimation(fadeIn)
                            binding.upFloatingBtn.visibility = View.VISIBLE
                            isScrolling = false
                        }


                    }

                    RecyclerView.SCROLL_STATE_DRAGGING -> {  // 스크롤 다시 시작하면 다시 버튼 숨키기
                        isScrolling = true
                    }
                }


                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    if (!isLoading && hasMoreData) {
                        isLoading = true
                        binding.infiniteLoadingProgressbar.visibility = View.VISIBLE
                        lastRowNUm += pageSize
                        val nextPageRequestData = SurveyRequestData(
                            userSn,
                            keyword = binding.surveyTitleSearchEditText.text.toString(),
                            pageSize,
                            lastRowNUm
                        )
                        requestSurvey(nextPageRequestData)

                    }


                }

            }
        })


        binding.upFloatingBtn.setOnClickListener {
            binding.surveyRecyclerView.smoothScrollToPosition(0)
        }


    }


    private fun requestSurvey(surveyRequestDataSet: SurveyRequestData) {
        surveyProvider.surveyGo(surveyRequestDataSet)
    }


    override fun completeSurvey(code: String, msg: String, resultData: List<SurveyResponse>) {
        codeA = code
        messageA = msg

        binding.progressBarLinearLayout.visibility = View.GONE

        binding.infiniteLoadingProgressbar.visibility = View.GONE

        isLoading = false

        if (codeA.equals("100")) {
            Log.d("success", codeA + ": " + messageA)

            for (dataItem in resultData) {
                surveySnA = dataItem.surveySn
            }
            // 상세 조회에 필요한 정보 저장
            SurveyInformation.saveSurveySn(surveySnA)


            if (resultData.isNullOrEmpty()) {
                hasMoreData = false
                Toast.makeText(this@SurveyActivity, "데이터없음", Toast.LENGTH_SHORT).show()
                // 데이터가 없으면

            } else {

                //Toast.makeText(this@LetterActivity, messageA, Toast.LENGTH_SHORT).show()
                hasMoreData = true
                // 데이터가 있으면 어댑터에 추가
                adapter.addData(resultData)


            }


        } else if (codeA.equals("E503")) {
            Log.d("error!!", codeA + ": " + messageA)
            Toast.makeText(this@SurveyActivity, messageA, Toast.LENGTH_SHORT)
                .show()
        } else if (codeA.equals("E600")) {
            Log.d("error!!", codeA + ": " + messageA)
            Toast.makeText(this@SurveyActivity, messageA, Toast.LENGTH_SHORT)
                .show()

        }


    }
}