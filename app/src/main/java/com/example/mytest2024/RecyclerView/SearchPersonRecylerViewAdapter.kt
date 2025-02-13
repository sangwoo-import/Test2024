package com.example.mytest2024.RecyclerView

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest2024.swaggerapi.Retrofit.SearchPerson
import com.example.mytest2024.swaggerapi.SearchUserInformation
import com.example.mytest2024.R
import com.example.mytest2024.SearchPersonDetailFragment


// view : item_list 를 의미
// 수정 하기 위한 이미지뷰 , text 가져옴 , 홀더 = 객체 저장 용도
class SearchPersonCustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var username = view.findViewById<TextView>(R.id.searchUsername)
    var upWardName = view.findViewById<TextView>(R.id.searchupWardName)
    var classNm = view.findViewById<TextView>(R.id.searchClassName)
    var wardName = view.findViewById<TextView>(R.id.searchWardName)
    var mobilePhoneNum = view.findViewById<TextView>(R.id.MobilePhoneNum)
}

// Adapter 에는 3가지가 필요하다
// 어댑터 에서 data 를 매개변수로 받아야한다. 또한 context 넣을자리에 편하게 넣기 위해 Context 매개변수 생성
class SearchPersonRecylerViewAdapter(
    // 원래는 List
    var dataSet: MutableList<SearchPerson>,
    val context: Context
) : RecyclerView.Adapter<SearchPersonCustomViewHolder>() {


    // 이미지를 생성하는 부분 = itemList를 recylerview xml에 붙이기 위함
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchPersonCustomViewHolder {
//        LayoutInflater : 레이아웃 팽창 , from : parent.context로 부터 , inflate : 팽창을 하는데 item_list 파일을 view로 만들기 위해
        val callForRow =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.all_searchpeson_item_list, parent, false)
        // ViewHolder에 반환한다
        return SearchPersonCustomViewHolder(callForRow)
    }

    // viewHolder의 갯수를 지정 =  data 갯수를 능동적으로 만들기 위해 size를 기입하면됨
    override fun getItemCount() = dataSet.size

    // 이미지및 view를 수정하는 부분 = 연결하는 부분(보여지는 부분)
    // 수정하기 위해 viewHolder에 작업이 먼저 필요
    // 클릭 기능 같은 경우 여기서 작업 하는게 좋음
    override fun onBindViewHolder(holder: SearchPersonCustomViewHolder, position: Int) {

        //  새로 만들어지는 리사이 클러 뷰에 적용이 된다
        // 이미지가 int값으로 데이터가 들어가잇어서 position을 넣으면 int형식인 이미지 한 부분만 수정한다.

        var curData = dataSet[position]



        holder.username.text = curData.userName
        if (curData.upWardNm.isNullOrEmpty()) {
            holder.upWardName.text = "소속없음"
//            holder.upWardName.setTextColor(Color.BLUE)
        } else {
            holder.upWardName.text = curData.upWardNm
            holder.upWardName.setTextColor(Color.BLACK)

        }
        if (curData.wardNm.isNullOrEmpty()) {
            holder.wardName.text = "부서없음"
//            holder.wardName.setTextColor(Color.BLUE)
        } else {
            holder.wardName.text = curData.wardNm
            holder.wardName.setTextColor(Color.BLACK)
        }
        if (curData.classNm.isNullOrEmpty()) {
            holder.classNm.text = "직급없음"
//            holder.classNm.setTextColor(Color.BLUE)
        } else {
            holder.classNm.text = curData.classNm
            holder.classNm.setTextColor(Color.BLACK)
        }
//        if (curData.mobileTelNo.isNullOrEmpty()) {
//            holder.mobilePhoneNum.text = "전화없음"
////            holder.mobilePhoneNum.setTextColor(Color.BLUE)
//        } else {
//            holder.mobilePhoneNum.text = curData.mobileTelNo
//            holder.mobilePhoneNum.setTextColor(Color.BLACK)
//        }


        // 클릭 했을 때
        holder.itemView.setOnClickListener {

            /* response 되는 userSn 이 각각의 정보로 변경함 으로서 상세 조회를 할 수 있어서 */
            SearchUserInformation.userSn = curData.userSn

            val dialog = SearchPersonDetailFragment()
            val activity = context as FragmentActivity
            val fragment = activity.supportFragmentManager
            dialog.show(fragment, "SearchPersonDetailFragment")

        }
    }


    fun addData(newData : List<SearchPerson>){
        val startPosition =dataSet.size
        dataSet.addAll(newData)
        notifyItemRangeInserted(startPosition,newData.size)
    }

    fun reset(){
        dataSet.clear()
        notifyDataSetChanged() // 어댑터에 변경 사항 통지
    }



}


