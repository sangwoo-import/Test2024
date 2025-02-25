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
import com.example.mytest2024.RecyclerView.FamilyEventRecyclerViewAdapter
import com.example.mytest2024.swaggerapi.FamilyEventInformation
import com.example.mytest2024.swaggerapi.Retrofit.FamilyEventRequestData
import com.example.mytest2024.swaggerapi.Retrofit.FamilyEventResponse
import com.example.mytest2024.swaggerapi.swaggercontroller.FamilyEventProvider
import com.example.mytest2024.databinding.ActivityFamilyEventBinding

class FamilyEventActivity : AppCompatActivity(), FamilyEventProvider.CallBack {

    private lateinit var binding: ActivityFamilyEventBinding
    private val familyEventProvider = FamilyEventProvider(this@FamilyEventActivity)

    private lateinit var adapter: FamilyEventRecyclerViewAdapter

    /*request Data*/
    private var keyword = ""
    private val pageSize = 50
    private var lastRowNUm = 0


    /* 게시글 상세 정보에 필요한 데이터*/

    private var boardIdA = ""
    private var postingSnA = 0


    /*Response data */
    private var codeA: String = ""
    private var messageA: String = ""

    /* 현재  recyclerView Loading 상태 */
    private var isLoading = false

    /* 더이상 데이터가 없거나 데이터가 없을때 스크롤 방지*/
    private var hasMoreData = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFamilyEventBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //  먼저 제일 먼저 생성
        val recyclerView = binding.FamilyEventRecyclerView
        recyclerView.setHasFixedSize(true)
        // LinearLayoutManager가 RecyclerViewActivity를 통제한다는 의미    LinearLayout 속성에 수직으로 정했으니 수직으로 스크롤 한다.
        recyclerView.layoutManager = LinearLayoutManager(this@FamilyEventActivity)
        // adapter 붙어주기에 만든 data를 붙여주기 + context
        adapter = FamilyEventRecyclerViewAdapter(mutableListOf(), this@FamilyEventActivity)
        recyclerView.adapter = adapter


        /* 들어 가자 마자 실행 */
        val familyEventRequestDataSet = FamilyEventRequestData(
            keyword, pageSize,
            lastRowNUm
        )
        requestFamilyEvent(familyEventRequestDataSet)


        binding.progressBarLinearLayout.visibility = View.VISIBLE



        /*검색창 EditText에 글자 입력 받으면 글자 삭제 버튼 보이고 없으면 삭제 버튼 안보이기 */
        binding.FamilyEventTitleSearchEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if (!s.isNullOrEmpty()) {
                    binding.FamilyEventCancelBtn.visibility = View.VISIBLE
                } else {
                    binding.FamilyEventCancelBtn.visibility = View.GONE
                }
            }
            override fun afterTextChanged(s: Editable?) {

                /* 이 조건은 글자 다 지웠을 때 바로 조회 안할려고 버튼 기능에 대한 역할을 줄려고 -> 그냥 글자
                 다 지워도 되고 */
                if(!s.isNullOrEmpty()&& s.length>1) {
                    // 입력 실시간
                    lastRowNUm = 0

                    // 어댑터 초기화  -> 이름을 검색하던 빈값을 검색하던 한번 다시 초기화 하고 표출 하기 위해서
                    adapter.reset()
                    val familyEventRequestData3 = FamilyEventRequestData(

                        binding.FamilyEventTitleSearchEditText.text.toString(),
                        pageSize,
                        lastRowNUm

                    )
                    requestFamilyEvent(familyEventRequestData3)
                }

            }

        }
        )



        /* EditText 적힌거 다 삭제 */
        binding.FamilyEventCancelBtn.setOnClickListener {
            binding.FamilyEventTitleSearchEditText.text.clear()
        }

        /* 제목 검색할 때 */

        binding.FamilyEventSearchBtn.setOnClickListener {
            lastRowNUm = 0

            // 어댑터 초기화
            adapter.reset()
            val familyEventRequestData2 = FamilyEventRequestData(

                binding.FamilyEventTitleSearchEditText.text.toString(),
                pageSize,
                lastRowNUm

            )
            requestFamilyEvent(familyEventRequestData2)
        }

        /* Floating button 구현 */
        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 1500 }
        val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 1500 }


        /* 스크롤 내렸을 때 */
        binding.FamilyEventRecyclerView.addOnScrollListener(object :
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
                        val nextPageRequestData = FamilyEventRequestData(
                            keyword = binding.FamilyEventTitleSearchEditText.text.toString(),
                            pageSize,
                            lastRowNUm
                        )
                        requestFamilyEvent(nextPageRequestData)

                    }


                }

            }
        })


        binding.upFloatingBtn.setOnClickListener {
            binding.FamilyEventRecyclerView.smoothScrollToPosition(0)
        }


    }

    private fun requestFamilyEvent(familyEventRequestData: FamilyEventRequestData) {
        familyEventProvider.FamilyEventGo(familyEventRequestData)
    }


    override fun completeFamilyEvent(
        code: String,
        msg: String,
        resultData: List<FamilyEventResponse>
    ) {
        codeA = code
        messageA = msg

        binding.progressBarLinearLayout.visibility = View.GONE

        binding.infiniteLoadingProgressbar.visibility = View.GONE
        isLoading = false


        if (codeA.equals("100")) {
            Log.d("success", codeA + ": " + messageA)

            // 게시글 상세 조회에 필요한 정보 저장


            for (dateItem in resultData) {
                boardIdA = dateItem.boardId
                postingSnA = dateItem.postingSn

            }

            /* 게시글 상세 정보 저장 */
            FamilyEventInformation.saveFamilyEventInfos(boardIdA, postingSnA)

            if (resultData.isNullOrEmpty()) {
                hasMoreData = false
                //Toast.makeText(this@FamilyEventActivity, "데이터없음", Toast.LENGTH_SHORT).show()
                // 데이터가 없으면

            } else {
                //Toast.makeText(this@FreeTalkActivity, messageA, Toast.LENGTH_SHORT).show()
                hasMoreData = true
                // 데이터가 있으면 어댑터에 추가
                adapter.addData(resultData)


            }


        } else if (codeA.equals("E503")) {
            Log.d("error!!", codeA + ": " + messageA)
            Toast.makeText(this@FamilyEventActivity, messageA, Toast.LENGTH_SHORT)
                .show()
        } else if (codeA.equals("E600")) {
            Log.d("error!!", codeA + ": " + messageA)
            Toast.makeText(this@FamilyEventActivity, messageA, Toast.LENGTH_SHORT)
                .show()

        } else {
            Log.d("error!!", codeA + ": " + messageA)
            Toast.makeText(this@FamilyEventActivity, messageA, Toast.LENGTH_SHORT)
                .show()
        }

    }

}