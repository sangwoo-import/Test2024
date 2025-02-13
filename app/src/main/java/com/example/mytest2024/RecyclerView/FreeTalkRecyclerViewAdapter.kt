package com.example.mytest2024.RecyclerView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest2024.BoardDetailActivity
import com.example.mytest2024.R
import com.example.mytest2024.swaggerapi.FreeTalkInformation
import com.example.mytest2024.swaggerapi.Retrofit.FreeTalkResponse


// view : item_list 를 의미
// 수정 하기 위한 이미지뷰 , text 가져옴 , 홀더 = 객체 저장 용도
class FreeTalkCustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var freeTalkUserName = view.findViewById<TextView>(R.id.familyEventUserName)
    var freeTalkRegisterDate = view.findViewById<TextView>(R.id.familyEventRegisterDate)
    var freeTalkTitle = view.findViewById<TextView>(R.id.familyEventTitle)

}


class FreeTalkRecyclerViewAdapter(
    // 원래는 List
    var dataSet: MutableList<FreeTalkResponse>,
    val context: Context
) : RecyclerView.Adapter<FreeTalkCustomViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FreeTalkCustomViewHolder {
        val callForRow =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.all_searchfreetalk_item_list, parent, false)
        return FreeTalkCustomViewHolder(callForRow)
    }

    override fun getItemCount() = dataSet.size


    override fun onBindViewHolder(holder: FreeTalkCustomViewHolder, position: Int) {

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
            FreeTalkInformation.boardId = curData.boardId
            FreeTalkInformation.postingSn = curData.postingSn

            val intent = Intent(context, BoardDetailActivity::class.java)
            intent.apply {
                context.startActivity(intent)

            }

        }


    }

    fun addData(newData: List<FreeTalkResponse>) {
        val startPosition = dataSet.size
        dataSet.addAll(newData)
        notifyItemRangeInserted(startPosition, newData.size)
    }

    fun reset() {
        dataSet.clear()
        notifyDataSetChanged() // 어댑터에 변경 사항 통지
    }
}