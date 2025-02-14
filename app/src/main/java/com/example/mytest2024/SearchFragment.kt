package com.example.mytest2024

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest2024.swaggerapi.swaggercontroller.SearchPersonProvider
import com.example.mytest2024.swaggerapi.SearchUserInformation
import com.example.mytest2024.swaggerapi.Retrofit.SearchPerson
import com.example.mytest2024.swaggerapi.Retrofit.SearchPersonRequestData
import com.example.mytest2024.RecyclerView.SearchPersonRecylerViewAdapter
import com.example.mytest2024.databinding.SearchFragmentBinding

class SearchFragment : Fragment(), SearchPersonProvider.CallBack {

    private lateinit var binding: SearchFragmentBinding

    private lateinit var adapter: SearchPersonRecylerViewAdapter
    private val searchPersonProvider = SearchPersonProvider(this@SearchFragment)

    /*request Data*/
    private var keyword = ""
    private val searchType = "SEARCH_NAME"
    private val pageSize = 50
    private var lastRowNUm = 0

    /*response Data 및  싱글 톤 저장*/
    private var codeA: String = ""
    private var messageA: String = ""
    private var resultSizeA = 0

    var userSnA: String = ""
    var userNameA: String = ""
    var upWardNameA: String = ""
    var wardNmA: String = ""
    var classNmA: String = ""
    var mobilePhoneNumA: String = ""


    /* 현재  recyclerView Loading 상태 */
    private var isLoading = false

    /* 더이상 데이터가 없거나 데이터가 없을때 스크롤 방지*/
    private var hasMoreData = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = SearchFragmentBinding.inflate(layoutInflater)

        //  먼저 제일 먼저 생성
        val recyclerView = binding.searchPersonrecyclerView
        recyclerView.setHasFixedSize(true)
        // LinearLayoutManager가 RecyclerViewActivity를 통제한다는 의미    LinearLayout 속성에 수직으로 정했으니 수직으로 스크롤 한다.
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // adapter 붙어주기에 만든 data를 붙여주기 + context
        adapter = SearchPersonRecylerViewAdapter(mutableListOf(), requireContext())
        recyclerView.adapter = adapter


        /* 들어 가자 마자 실행*/
        val searchPersonRequestDataSet = SearchPersonRequestData(
            keyword,
            searchType,
            pageSize,
            lastRowNUm

        )
        requestSearchPerson(searchPersonRequestDataSet)

        binding.progressBarLinearLayout.visibility = View.VISIBLE


        /*검색창 EditText에 글자 입력 받으면 글자 삭제 버튼 보이고 없으면 삭제 버튼 안보이기 */
        binding.searchPersonEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.serachCancleBtn.visibility = View.VISIBLE
                } else {
                    binding.serachCancleBtn.visibility = View.GONE
                }


            }

            override fun afterTextChanged(s: Editable?) {
                /* 이 조건은 글자 다 지웠을 때 바로 조회 안 할려고 버튼 기능에 대한 역할을 줄려고 -> 그냥 글자
                                 다 지워도 되고 */

                // 입력 실시간
                lastRowNUm = 0

                // 어댑터 초기화  -> 이름을 검색하던 빈값을 검색하던 한번 다시 초기화 하고 표출 하기 위해서
                adapter.reset()
                val searchPersonRequestDataSet3 = SearchPersonRequestData(
                    s.toString(),
                    searchType,
                    pageSize,
                    lastRowNUm

                )
                requestSearchPerson(searchPersonRequestDataSet3)

            }

        }

        )


        /* EditText 적힌거 다 삭제 */
        binding.serachCancleBtn.setOnClickListener {
            binding.searchPersonEditText.text.clear()
        }


        /* 검색 버튼 눌렀을 때 */
        binding.personSearchBtn.setOnClickListener {

            // 다시 초기화 시킬려고
            lastRowNUm = 0

            // 어댑터 초기화  -> 이름을 검색하던 빈값을 검색하던 한번 다시 초기화 하고 표출 하기 위해서
            adapter.reset()
            val searchPersonRequestDataSet2 = SearchPersonRequestData(
                binding.searchPersonEditText.text.toString(),
                searchType,
                pageSize,
                lastRowNUm

            )
            requestSearchPerson(searchPersonRequestDataSet2)

        }


        /* Floating button Animation*/
        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 1500 }
        val fadeOut = AlphaAnimation(1f, 0f).apply { duration = 1500 }


        /* 스크롤 조작 함수  */
        binding.searchPersonrecyclerView.addOnScrollListener(object :
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


                // 무한 스크롤 -> 밑에 도달 하면서 멈추면(Scroll_State_IDLE)   다시 API 호출
                // 리스트가 더 이상 아래로 스크롤 되지 않을 때 && 스크롤이 멈출 때
                // vertical  0이 젤위 젤  밑이 1
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE
                ) {

                    if (!isLoading && hasMoreData) {

                        isLoading = true
                        binding.infiniteLoadingProgressbar.visibility = View.VISIBLE
                        lastRowNUm += pageSize
                        val nextPageRequestData = SearchPersonRequestData(
                            // 이름이 있던 없던 간에 계속 호출 할때는 검색창에 이름이 유지 되어야
                            // 같은 이름으로 호출 할 수 있으니깐
                            keyword = binding.searchPersonEditText.text.toString(),
                            searchType,
                            pageSize,
                            lastRowNUm
                        )
                        requestSearchPerson(nextPageRequestData)

                    }


                }
            }
        })


        binding.upFloatingBtn.setOnClickListener {
            binding.searchPersonrecyclerView.smoothScrollToPosition(0)
        }


        return binding.root // root는 contraint 전체를 뜻한다
    }

    fun requestSearchPerson(searchPersonRequestDataSet: SearchPersonRequestData) {
        searchPersonProvider.SearchPersonGo(searchPersonRequestDataSet)
    }


    // 직원 목록 조회는 일단 다 100으로 성공 시킨다.
    override fun completeSearchPerson(
        code: String,
        msg: String,
        resultSize: Int,
        resultData: List<SearchPerson>
    ) {
        codeA = code
        messageA = msg
        resultSizeA = resultSize

        binding.progressBarLinearLayout.visibility = View.GONE

        binding.infiniteLoadingProgressbar.visibility = View.GONE
        isLoading = false

        if (codeA.equals("100")) {
            for (dataItem in resultData) {
                userSnA = dataItem.userSn
                userNameA = dataItem.userName
                upWardNameA = dataItem.upWardNm
                wardNmA = dataItem.wardNm
                classNmA = dataItem.classNm
                mobilePhoneNumA = dataItem.mobileTelNo

            }


            // 데이터 따로 저장 상세 직원 목록 상세 조회 할 때 저장 용도
            SearchUserInformation.saveSearchUserInfo(
                userSnA,
                userNameA,
                upWardNameA,
                wardNmA,
                classNmA,
                mobilePhoneNumA
            )


            if (resultData.isNullOrEmpty()) {
                hasMoreData = false
                //Toast.makeText(requireContext(), "데이터가 없습니다!", Toast.LENGTH_SHORT).show()
                // 데이터가 없으면

            } else {
                //Toast.makeText(requireContext(), messageA, Toast.LENGTH_SHORT).show()
                hasMoreData = true
                // 데이터가 있으면 어댑터에 추가
                adapter.addData(resultData)

            }

        }

    }


}