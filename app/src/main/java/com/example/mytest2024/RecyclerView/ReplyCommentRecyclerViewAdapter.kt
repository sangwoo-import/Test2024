package com.example.mytest2024.RecyclerView

import android.annotation.SuppressLint
import android.content.Context
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest2024.R
import com.example.mytest2024.SwaggerAPI.Retrofit.BoardCommentListData
import com.example.mytest2024.SwaggerAPI.Retrofit.ChildResponseData

// view : item_list 를 의미
// 수정 하기 위한 이미지뷰 , text 가져옴 , 홀더 = 객체 저장 용도
class ReplyCommentCustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var boardDetailUserName = view.findViewById<TextView>(R.id.familyEventUserName)
    var boardDetailRegisterDate = view.findViewById<TextView>(R.id.familyEventRegisterDate)
    var boardDetailComment = view.findViewById<TextView>(R.id.freeTalkComment)
    var likeCount = view.findViewById<TextView>(R.id.likeCount)
    var sadCount = view.findViewById<TextView>(R.id.sadCount)

}


class ReplyCommentRecyclerViewAdapter(
    // 원래는 List
    var dataSet: List<ChildResponseData>,
    val context: Context
) : RecyclerView.Adapter<ReplyCommentCustomViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReplyCommentCustomViewHolder {
        val callForRow =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.all_board_detail_item_second_list, parent, false)
        return ReplyCommentCustomViewHolder(callForRow)
    }

    override fun getItemCount() = dataSet.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ReplyCommentCustomViewHolder, position: Int) {


        var curData = dataSet[position]


        if (curData.writerName.isNullOrEmpty()) {
            holder.boardDetailUserName.text = "이름없음"
        } else {
            holder.boardDetailUserName.text = curData.writerName
        }

        if (curData.registerDate.isNullOrEmpty()) {
            holder.boardDetailRegisterDate.text = "날짜없음"
        } else {
            holder.boardDetailRegisterDate.text = curData.registerDate
        }

        if (curData.commentContent.isNullOrEmpty()) {
            holder.boardDetailComment.text = "댓글 비어있음"
        } else {
            holder.boardDetailComment.text = HtmlCompat.fromHtml(
                curData.commentContent,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            holder.boardDetailComment.autoLinkMask = Linkify.WEB_URLS
            holder.boardDetailComment.linksClickable = true

        }
        holder.likeCount.text = curData.likeCount.toString()
        holder.sadCount.text = curData.dislikeCount.toString()


    }
}