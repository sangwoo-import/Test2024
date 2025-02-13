package com.example.mytest2024.RecyclerView

import JugwanSurveyRecyclerViewAdapter
import com.example.mytest2024.swaggerapi.Retrofit.SurveyQuestionList

import android.annotation.SuppressLint
import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.example.mytest2024.R


/* 현재 전체 질문 리스트 보여주는 Adapter*/

class SurveyDetailCustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val surveyQuestionSn: TextView = view.findViewById(R.id.surveyQuestionSn)
    val surveyQuestionNm: TextView = view.findViewById(R.id.surveyQuestionNm)
    val surveyQuestionRecyclerView: RecyclerView =
        view.findViewById(R.id.surveyQuestionRecyclerView)
}


// 원래 어댑터에 BoardDetailCustomViewHolder이거 넣어야함
class SurveyDetailRecyclerViewAdapter(
    // 원래는 List
    var dataSet: MutableList<SurveyQuestionList>,
    val context: Context,
    var answerAt: String
) : RecyclerView.Adapter<SurveyDetailCustomViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SurveyDetailCustomViewHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.survey_question_item, parent, false)
        return SurveyDetailCustomViewHolder(view)


    }

    override fun getItemCount() = dataSet.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SurveyDetailCustomViewHolder, position: Int) {

        var curData = dataSet[position]


        /* 질문 번호 및 질문 내용 */
        holder.surveyQuestionSn.text = curData.surveyQuestionSn + "."
        holder.surveyQuestionNm.text = curData.questionNm


        /*객관식 주관식 recyclerview 생성 */
        val surveyQuestionRecyclerview = holder.surveyQuestionRecyclerView


        /*객관식  어댑터 생성 후 어댑터 붙이기 */
        if (curData.questionSeCd.equals("47100013")) {

            if (curData.optionList.isNullOrEmpty()) {
                surveyQuestionRecyclerview.visibility = View.GONE
            } else {
                /* 멀티 체크 여부 보내기 */
                println("firstGaekMultiCheck " + curData.multiCheckAt)
                surveyQuestionRecyclerview.layoutManager =
                    LinearLayoutManager(holder.itemView.context)


                val gaekAdapter = GaekSurveyRecyclerVIewAdapter(
                    curData.optionList,
                    context,
                    curData.multiCheckAt,
                    answerAt
                )
                surveyQuestionRecyclerview.adapter = gaekAdapter
                surveyQuestionRecyclerview.visibility = View.VISIBLE
            }


            /*주관식  어댑터 생성 후 어댑터 붙이기 */
        } else if (curData.questionSeCd.equals("47100014")) {

            if (curData.optionList.isNullOrEmpty()) {
                surveyQuestionRecyclerview.visibility = View.GONE
            } else {
                /* 멀티 체크 여부 보내기 저장 */
                println("firstJugwanMultiCheck " + curData.multiCheckAt)

                surveyQuestionRecyclerview.layoutManager =
                    LinearLayoutManager(holder.itemView.context)



                val jugwanAdapter = JugwanSurveyRecyclerViewAdapter(
                    curData.optionList,
                    context,
                    curData.multiCheckAt,
                    answerAt
                )

                surveyQuestionRecyclerview.adapter = jugwanAdapter
                surveyQuestionRecyclerview.visibility = View.VISIBLE
            }


        }


    }


    fun updateData(newData: List<SurveyQuestionList>) {
        dataSet.clear()
        dataSet.addAll(newData)
        notifyDataSetChanged()
    }

    fun addData(newData: List<SurveyQuestionList>) {
        val startPosition = dataSet.size
        dataSet.addAll(newData)
        notifyItemRangeInserted(startPosition, newData.size)
    }
}