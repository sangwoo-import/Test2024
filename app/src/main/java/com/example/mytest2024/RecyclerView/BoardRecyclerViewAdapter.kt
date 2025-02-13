package com.example.mytest2024.RecyclerView

import android.annotation.SuppressLint
import android.content.Context
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest2024.R
import com.example.mytest2024.swaggerapi.Retrofit.BoardCommentListData


class BoardDetailCustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val boardDetailUserName: TextView = view.findViewById(R.id.familyEventUserName)
    val boardDetailRegisterDate: TextView = view.findViewById(R.id.familyEventRegisterDate)
    val boardDetailComment: TextView = view.findViewById(R.id.freeTalkComment)
    val likeCount: TextView = view.findViewById(R.id.likeCount)
    val sadCount: TextView = view.findViewById(R.id.sadCount)
    val childRecyclerView: RecyclerView = view.findViewById(R.id.replyRecyclerView)
}


// 원래 어댑터에 BoardDetailCustomViewHolder이거 넣어야함
class BoardRecyclerViewAdapter(
    // 원래는 List
    var dataSet: MutableList<BoardCommentListData>,
    val context: Context
) : RecyclerView.Adapter<BoardDetailCustomViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardDetailCustomViewHolder {


        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.all_board_detail_item_list, parent, false)
        return BoardDetailCustomViewHolder(view)


    }

    override fun getItemCount() = dataSet.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BoardDetailCustomViewHolder, position: Int) {



        /* 즉 이게 dataSet의  이중 for문*/
//        boardlist 사이즈 만큼 도는데  그 안에 child list 만큼 돈다.

        /* for(int i =0; i< boardlist.size(); i++){
              for(int j=0; j<boardlist.childlist.size(); j++){

              system.out.println( i+j)   ==dataSet.get(i).childCommentList.get(j)   이런식
         */


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



        val childRecyclerView = holder.childRecyclerView
        if (curData.childCommentList.isNullOrEmpty()) {
            childRecyclerView.visibility = View.GONE
        } else {
            childRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
            val childAdapter = ReplyCommentRecyclerViewAdapter(curData.childCommentList, context)
            childRecyclerView.adapter = childAdapter
            childRecyclerView.visibility = View.VISIBLE


        }




    }


    fun updateData(newData: List<BoardCommentListData>) {
        dataSet.clear()
        dataSet.addAll(newData)
        notifyDataSetChanged()
    }

    fun addData(newData: List<BoardCommentListData>) {
        val startPosition = dataSet.size
        dataSet.addAll(newData)
        notifyItemRangeInserted(startPosition, newData.size)
    }
}