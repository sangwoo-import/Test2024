package com.example.mytest2024

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.util.Linkify
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytest2024.RecyclerView.BoardRecyclerViewAdapter
import com.example.mytest2024.SwaggerAPI.FamilyEventInformation
import com.example.mytest2024.SwaggerAPI.FreeTalkInformation
import com.example.mytest2024.SwaggerAPI.Retrofit.BoardCommentListData
import com.example.mytest2024.SwaggerAPI.Retrofit.BoardDetailRequestData
import com.example.mytest2024.SwaggerAPI.Retrofit.BoardDetailResponse
import com.example.mytest2024.SwaggerAPI.Retrofit.BoardFileListData
import com.example.mytest2024.SwaggerAPI.SwaggerController.BoardDetailProvider
import com.example.mytest2024.databinding.ActivityBoardDetailBinding
import com.example.mytest2024.databinding.ActivityBoardDetailFamilyBinding

class BoardDetailFamilyActivity : AppCompatActivity(), BoardDetailProvider.CallBack {

    private lateinit var binding: ActivityBoardDetailFamilyBinding

    private val boardDetailProvider = BoardDetailProvider(this@BoardDetailFamilyActivity)

    private lateinit var adapter: BoardRecyclerViewAdapter


    /*Response data */
    private var codeA: String = ""
    private var messageA: String = ""


    private var writerNameA: String? = null
    private var titleA: String = ""
    private var registerDateA: String? = null
    private var boardContentA: String = ""

    private var likeCountA: Int = 0
    private var sadCountA: Int = 0
    private var loveCountA: Int = 0
    private var bestCountA: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBoardDetailFamilyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //  먼저 제일 먼저 생성
        val recyclerView = binding.boardDetailRecyclerView
        recyclerView.setHasFixedSize(true)
        // LinearLayoutManager가 RecyclerViewActivity를 통제한다는 의미    LinearLayout 속성에 수직으로 정했으니 수직으로 스크롤 한다.
        recyclerView.layoutManager = LinearLayoutManager(this@BoardDetailFamilyActivity)
        // adapter 붙어주기에 만든 data를 붙여주기 + context
        adapter = BoardRecyclerViewAdapter(mutableListOf(), this@BoardDetailFamilyActivity)
        recyclerView.adapter = adapter


        // 통신 타이밍 안맞아서 먼저 보여주고 바뀌니깐
        // 일단 여기들어오면 바로 안보이게 하고 성공과 실패되면 보여주고 안보여주고 할려고
        binding.boardDetailScrollView.visibility = View.GONE


        /*request Data*/
        val bbsId = FamilyEventInformation.boardId
        val postingSn = FamilyEventInformation.postingSn

        /* 들어 가자 마자 실행 */
        val boardRequestDataSet = BoardDetailRequestData(
            bbsId.toString(), postingSn!!.toInt()
        )
        requestBoard(boardRequestDataSet)

        binding.progressBarLinearLayout.visibility = View.VISIBLE


    }


    private fun requestBoard(boardRequestDataSet: BoardDetailRequestData) {
        boardDetailProvider.boardDetailGo(boardRequestDataSet)
    }


    @SuppressLint("SetTextI18n")
    override fun completeBoardDetail(
        code: String,
        msg: String,
        resultData: List<BoardDetailResponse>,
        boardFileListData: List<BoardFileListData>,
        boardCommentList: List<BoardCommentListData>,
       // childCommentList: List<BoardCommentListData>
    ) {

        codeA = code
        messageA = msg
        binding.boardDetailScrollView.visibility = View.VISIBLE
        binding.progressBarLinearLayout.visibility = View.GONE




        if (codeA.equals("100")) {

            for (dateItem in resultData) {

                writerNameA = dateItem.writerName
                titleA = dateItem.title
                registerDateA = dateItem.registerDate
                boardContentA = dateItem.content
                likeCountA = dateItem.emtcnLike
                sadCountA = dateItem.emtcnSad
                loveCountA = dateItem.emtcnLove
                bestCountA = dateItem.emtcnBest
            }




            if (writerNameA.isNullOrEmpty()) {
                binding.familyEventUserName.text = "이름없음"
            } else {
                binding.familyEventUserName.text = writerNameA
            }

            if (registerDateA.isNullOrEmpty()) {
                binding.familyEventRegisterDate.text = "날짜없음"
            } else {
                binding.familyEventRegisterDate.text = registerDateA
            }
            if (titleA.isNullOrEmpty()) {
                binding.familyEventTitle.text = "데이터없음"
            } else {
                binding.familyEventTitle.text = titleA
            }
            if (boardContentA.isNullOrEmpty()) {
                binding.boardContent.text = "데이터 없음"
            } else {
                binding.boardContent.text = HtmlCompat.fromHtml(
                    boardContentA,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                binding.boardContent.autoLinkMask = Linkify.WEB_URLS
                binding.boardContent.linksClickable = true
            }
            /* 숫자는 기본이 0으로 나오니깐 그냥 넣자 */
            binding.likeCount.text = likeCountA.toString()
            binding.sadCount.text = sadCountA.toString()
            binding.loveCount.text = loveCountA.toString()
            binding.bestCount.text = bestCountA.toString()


            if (boardCommentList.isNullOrEmpty()) {
                binding.emptyComment.visibility = View.VISIBLE
                binding.boardDetailRecyclerView.visibility = View.GONE
                binding.spaceCardView.visibility = View.GONE
            } else {
                // 데이터가 있으면 어댑터에 추가
                adapter.addData(boardCommentList)

                binding.boardDetailRecyclerView.visibility = View.VISIBLE
                binding.emptyComment.visibility = View.GONE
            }


        } else {
            binding.boardDetailScrollView.visibility = View.GONE
            Log.d("error!!", codeA + ": " + messageA)
            Toast.makeText(this@BoardDetailFamilyActivity, messageA, Toast.LENGTH_SHORT).show()
        }


    }
}