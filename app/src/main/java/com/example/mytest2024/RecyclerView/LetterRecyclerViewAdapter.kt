package com.example.mytest2024.RecyclerView

import android.app.AlertDialog
import com.example.mytest2024.swaggerapi.Retrofit.LetterResponse

import android.content.Context
import android.content.Intent
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest2024.LetterDetailActivity
import com.example.mytest2024.swaggerapi.LetterInformation
import com.example.mytest2024.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


// view : item_list 를 의미
// 수정 하기 위한 이미지뷰 , text 가져옴 , 홀더 = 객체 저장 용도
class LetterCustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var imageProfile = view.findViewById<ImageView>(R.id.userProfileImageView)
    var letterTitle = view.findViewById<TextView>(R.id.letterTitleTextView)
    var sendUsername = view.findViewById<TextView>(R.id.sendUserNameTextView)
    var sendDate = view.findViewById<TextView>(R.id.sendDate)
    var passwordAt = view.findViewById<ImageView>(R.id.passWordIcon)
    var letterSawDefaultImage = view.findViewById<ImageView>(R.id.letterImageView)
    var letterSawImage = view.findViewById<ImageView>(R.id.letterImageView2)
    var letterSawText = view.findViewById<TextView>(R.id.letterSaw)
}


// Adapter 에는 3가지가 필요하다
// 어댑터 에서 data 를 매개변수로 받아야한다. 또한 context 넣을자리에 편하게 넣기 위해 Context 매개변수 생성
class LetterRecyclerViewAdapter(
    // 원래는 List
    var dataSet: MutableList<LetterResponse>,
    val context: Context
) : RecyclerView.Adapter<LetterCustomViewHolder>() {


    // 이미지를 생성하는 부분 = itemList를 recylerview xml에 붙이기 위함
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LetterCustomViewHolder {
//        LayoutInflater : 레이아웃 팽창 , from : parent.context로 부터 , inflate : 팽창을 하는데 item_list 파일을 view로 만들기 위해
        val callForRow =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.all_searchletter_item_list, parent, false)
        // ViewHolder에 반환한다
        return LetterCustomViewHolder(callForRow)
    }

    // viewHolder의 갯수를 지정 =  data 갯수를 능동적으로 만들기 위해 size를 기입하면됨
    override fun getItemCount() = dataSet.size

    // 이미지및 view를 수정하는 부분 = 연결하는 부분(보여지는 부분)
    // 수정하기 위해 viewHolder에 작업이 먼저 필요
    // 클릭 기능 같은 경우 여기서 작업 하는게 좋음
    override fun onBindViewHolder(holder: LetterCustomViewHolder, position: Int) {

        //  새로 만들어지는 리사이 클러 뷰에 적용이 된다
        // 이미지가 int값으로 데이터가 들어가잇어서 position을 넣으면 int형식인 이미지 한 부분만 수정한다.

        var curData = dataSet[position]


        /* Glide 사용 */

        // 임시 이미지
        val image = "https://newsimg.sedaily.com/2021/12/09/22V85NTJGY_1.jpg"

        // 프로필 사진 넣을 거면 주석 풀고 xml 에 UI 다시 추가하기
//        if (curData.userProfileImage.isNullOrEmpty()) {
//            holder.imageProfile.visibility = View.VISIBLE
//        } else {
//            // 원래 load에 curData.userProfileImage
//            Glide.with(context).load(curData.userProfileImage).centerCrop()
//                .into(holder.imageProfile)
//
//        }

        // 편지 제목
        if (curData.letterTitle.isNullOrEmpty()) {
            holder.letterTitle.text = "제목 없음"
        } else {
            holder.letterTitle.text = curData.letterTitle
        }

        // 보낸사람
        if (curData.senderUserName.isNullOrEmpty()) {
            holder.sendUsername.text = "없음"
        } else {
            holder.sendUsername.text = curData.senderUserName
        }
        // 보낸 날짜
        if (curData.sendDate.isNullOrEmpty()) {
            holder.sendDate.text = "데이터 없음"
        } else {
            holder.sendDate.text = curData.sendDate
        }
        /*비밀 번호가 존재하면 잠금 ICON 보여주기*/

        if (curData.passwordAt.isNullOrEmpty()) {
            holder.passwordAt.visibility = View.GONE
        } else {
            if (curData.passwordAt.equals("N")) {
                holder.passwordAt.visibility = View.GONE

            } else {
                holder.passwordAt.visibility = View.VISIBLE
            }
        }
        /* 편지 읽었나 안 읽었나 여부 */
        if (curData.receiveAt.equals("Y")) {
            holder.letterSawDefaultImage.visibility = View.GONE
            holder.letterSawImage.visibility = View.VISIBLE
            holder.letterSawText.text = "읽음"
        } else {
            holder.letterSawImage.visibility = View.GONE
            holder.letterSawDefaultImage.visibility = View.VISIBLE
            holder.letterSawText.text = "읽지않음"
        }


        // 클릭 했을 때
        holder.itemView.setOnClickListener {
            /* response 되는 userSn 으로 상세 조회를 할 수 있어서 */
            LetterInformation.letterSn = curData.letterSn

            if (curData.passwordAt.equals("Y")) {
                dialogMessageBox(context)
            } else {
                /* 비밀번호가 존재하지 않으면 null이든 뭐든 다 들어가도 노 상관*/
                LetterInformation.savePassWord("")
                val intent = Intent(context, LetterDetailActivity::class.java)
                intent.apply {
                    context.startActivity(intent)

                }

            }


        }


    }

    fun addData(newData: List<LetterResponse>) {
        val startPosition = dataSet.size
        dataSet.addAll(newData)
        notifyItemRangeInserted(startPosition, newData.size)
    }

    fun reset() {
        dataSet.clear()
        notifyDataSetChanged() // 어댑터에 변경 사항 통지
    }

    fun dialogMessageBox(context: Context) {

        val textInputLayout = TextInputLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            isPasswordVisibilityToggleEnabled = true
        }
        val passwordInput = TextInputEditText(context).apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            hint = "비밀번호를 입력하세요."

        }
        textInputLayout.addView(passwordInput)

        val dialog = AlertDialog.Builder(context)
            .setTitle("비밀번호를 입력해주세요.")
            .setView(textInputLayout)
            .setPositiveButton("확인") { dial, which ->
                val password = passwordInput.text.toString()

                /* 상세조회에 필요한 pw 저장 */
                LetterInformation.savePassWord(password)

                dial.dismiss()
                /*여기서 편지 상세 조회  화면 이동") */
                val intent = Intent(context, LetterDetailActivity::class.java)
                intent.apply {
                    context.startActivity(intent)
                }

            }
            .setNegativeButton("취소") { dial, which ->
                dial.dismiss()
            }

        dialog.show()


    }


}




