package com.example.mytest2024.RecyclerView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest2024.R
import com.example.mytest2024.SurveyDetailActivity
import com.example.mytest2024.swaggerapi.Retrofit.SurveyResponse
import com.example.mytest2024.swaggerapi.SurveyInformation


// view : item_list 를 의미
// 수정 하기 위한 이미지뷰 , text 가져옴 , 홀더 = 객체 저장 용도
class SurveyCustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var surveyTitle = view.findViewById<TextView>(R.id.SurveyTitleTextView)
    var beginDate = view.findViewById<TextView>(R.id.beginDateTextView)
    var endDate = view.findViewById<TextView>(R.id.endDateTextView)
}


class SurveyRecyclerViewAdapter(
    // 원래는 List
    var dataSet: MutableList<SurveyResponse>,
    val context: Context
) : RecyclerView.Adapter<SurveyCustomViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SurveyCustomViewHolder {
        val callForRow =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.all_searchsurvey_item_list, parent, false)
        return SurveyCustomViewHolder(callForRow)
    }

    override fun getItemCount() = dataSet.size


    override fun onBindViewHolder(holder: SurveyCustomViewHolder, position: Int) {

        //  새로 만들어지는 리사이 클러 뷰에 적용이 된다
        // 이미지가 int값으로 데이터가 들어가잇어서 position을 넣으면 int형식인 이미지 한 부분만 수정한다.

        var curData = dataSet[position]


        if (curData.surveyTitle.isNullOrEmpty()) {
            holder.surveyTitle.text = "데이터 없음"
        } else {
            holder.surveyTitle.text = curData.surveyTitle
        }
        if (curData.beginDate.isNullOrEmpty()) {
            holder.beginDate.text = "시작 날짜 데이터 없음"
        } else {
            holder.beginDate.text = curData.beginDate
        }
        if (curData.endDate.isNullOrEmpty()) {
            holder.endDate.text = "응시 날짜 데이터 없음"
        } else {
            holder.endDate.text = curData.endDate
        }


        /* 상세 페이지 */
        holder.itemView.setOnClickListener {
            SurveyInformation.surveySn = curData.surveySn

            val intent = Intent(context, SurveyDetailActivity::class.java)
            intent.apply {
                context.startActivity(intent)

            }

        }


    }

    fun addData(newData: List<SurveyResponse>) {
        val startPosition = dataSet.size
        dataSet.addAll(newData)
        notifyItemRangeInserted(startPosition, newData.size)
    }

    fun reset() {
        dataSet.clear()
        notifyDataSetChanged() // 어댑터에 변경 사항 통지
    }
}