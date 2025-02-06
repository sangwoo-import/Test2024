package com.example.mytest2024.RecyclerView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest2024.BoardDetailActivity
import com.example.mytest2024.BoardDetailFamilyActivity
import com.example.mytest2024.R
import com.example.mytest2024.SwaggerAPI.FamilyEventInformation
import com.example.mytest2024.SwaggerAPI.FreeTalkInformation
import com.example.mytest2024.SwaggerAPI.Retrofit.FamilyEventResponse
import com.example.mytest2024.SwaggerAPI.Retrofit.FreeTalkResponse

class FamilyEventCustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var freeTalkUserName = view.findViewById<TextView>(R.id.familyEventUserName)
    var freeTalkRegisterDate = view.findViewById<TextView>(R.id.familyEventRegisterDate)
    var freeTalkTitle = view.findViewById<TextView>(R.id.familyEventTitle)

}


class FamilyEventRecyclerViewAdapter(
    // 원래는 List
    var dataSet: MutableList<FamilyEventResponse>,
    val context: Context
) : RecyclerView.Adapter<FamilyEventCustomViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FamilyEventCustomViewHolder {
        val callForRow =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.all_searchfamilyevent_item_list, parent, false)
        return FamilyEventCustomViewHolder(callForRow)
    }

    override fun getItemCount() = dataSet.size


    override fun onBindViewHolder(holder: FamilyEventCustomViewHolder, position: Int) {

        //  새로 만들어지는 리사이 클러 뷰에 적용이 된다
        // 이미지가 int값으로 데이터가 들어가잇어서 position을 넣으면 int형식인 이미지 한 부분만 수정한다.

        var curData = dataSet[position]


        if (curData.writerName.isNullOrEmpty()) {
            holder.freeTalkUserName.text = "이름 없음"
        } else {
            holder.freeTalkUserName.text = curData.writerName
        }
        if (curData.registerData.isNullOrEmpty()) {
            holder.freeTalkRegisterDate.text = "날짜 없음"
        } else {
            holder.freeTalkRegisterDate.text = curData.registerData
        }
        if (curData.title.isNullOrEmpty()) {
            holder.freeTalkTitle.text = "제목 데이터 없음"
        } else {
            holder.freeTalkTitle.text = curData.title
        }


        /* 상세 페이지 */
        holder.itemView.setOnClickListener {
            FamilyEventInformation.boardId = curData.boardId
            FamilyEventInformation.postingSn = curData.postingSn

            val intent = Intent(context, BoardDetailFamilyActivity::class.java)
            intent.apply {
                context.startActivity(intent)

            }

        }


    }

    fun addData(newData: List<FamilyEventResponse>) {
        val startPosition = dataSet.size
        dataSet.addAll(newData)
        notifyItemRangeInserted(startPosition, newData.size)
    }

    fun reset() {
        dataSet.clear()
        notifyDataSetChanged() // 어댑터에 변경 사항 통지
    }
}