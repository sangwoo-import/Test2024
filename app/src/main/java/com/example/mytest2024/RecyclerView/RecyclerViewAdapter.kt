package com.example.mytest2024.RecyclerView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mytest2024.R


// view : item_list 를 의미
// 수정 하기 위한 이미지뷰 , text 가져옴 , 홀더 = 객체 저장 용도
class CustomViewHodler(view: View) : RecyclerView.ViewHolder(view) {
    val profile = view.findViewById<ImageView>(R.id.item_imageView)
    val name = view.findViewById<TextView>(R.id.itemTextView)
}

// Adapter 에는 3가지가 필요하다
// 어댑터 에서 data 를 매개변수로 받아야한다. 또한 context 넣을자리에 편하게 넣기 위해 Context 매개변수 생성
class RecyclerViewAdapter(var dataSet: ArrayList<RecyclerViewData>, val context: Context) :
    RecyclerView.Adapter<CustomViewHodler>() {

    // 이미지를 생성하는 부분 = itemList를 recylerview xml에 붙이기 위함
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHodler {
//        LayoutInflater : 레이아웃 팽창 , from : parent.context로 부터 , inflate : 팽창을 하는데 item_list 파일을 view로 만들기 위해
        val callForRow =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        // ViewHolder에 반환한다
        return CustomViewHodler(callForRow)
    }

    // viewHolder의 갯수를 지정 =  data 갯수를 능동적으로 만들기 위해 size를 기입하면됨
    override fun getItemCount() = dataSet.size

    // 이미지및 view를 수정하는 부분 = 연결하는 부분(보여지는 부분)
    // 수정하기 위해 viewHolder에 작업이 먼저 필요
    // 클릭 기능 같은 경우 여기서 작업 하는게 좋음
    override fun onBindViewHolder(holder: CustomViewHodler, position: Int) {

        //  새로 만들어지는 리사이 클러 뷰에 적용이 된다
        // 이미지가 int값으로 데이터가 들어가잇어서 position을 넣으면 int형식인 이미지 한 부분만 수정한다.

        val curData = dataSet[position]

        /* Coil 사용 */

//        holder.profile.load(curData.profile){
//            scale(Scale.FIT) // 이미지를 ImageView에 맞게 비율 유지하면서 맞추기
//        }


        /* Glide 사용 */
        Glide.with(context).load(curData.profile).into(holder.profile)

        // string 값인데 position?? int값
        holder.name.text = curData.name


        // 클릭 했을 때
        holder.itemView.setOnClickListener {
            Toast.makeText(context, curData.name, Toast.LENGTH_SHORT).show()
        }
    }
}