package com.example.mytest2024.RecyclerView

import android.widget.CheckBox
import com.example.mytest2024.swaggerapi.Retrofit.SurveyOptionListResponse


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest2024.R

import com.example.mytest2024.swaggerapi.Retrofit.SurveyAnswerSaveQuestionRequestData
import com.example.mytest2024.swaggerapi.SurveyInformation


// view : item_list 를 의미
// 수정 하기 위한 이미지뷰 , text 가져옴 , 홀더 = 객체 저장 용도
class GaekSurveyCustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var questionOptionContent = view.findViewById<CheckBox>(R.id.surveyGaekquestionContentCheckBox)


}


class GaekSurveyRecyclerVIewAdapter(
    // 원래는 List
    var dataSet: List<SurveyOptionListResponse>,
    val context: Context,
    var surveyType: String,
    var answerAt : String
) : RecyclerView.Adapter<GaekSurveyCustomViewHolder>() {


    // 단일 선택일 경우 선택된 위치 저장
    private var selectedPosition: Int = -1


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GaekSurveyCustomViewHolder {
        val callForRow =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.survey_gaek_item_list, parent, false)
        return GaekSurveyCustomViewHolder(callForRow)
    }

    override fun getItemCount() = dataSet.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GaekSurveyCustomViewHolder, position: Int) {

        var curData = dataSet[position]




        if (curData.questionOptionContent.isNullOrEmpty()) {
            holder.questionOptionContent.text = "답변 데이터없음"
        } else {
            holder.questionOptionContent.text = curData.questionOptionContent
        }


        /*답변이 완료 된거이면 체크박스 비활성화*/

        if(answerAt.equals("Y")){
            holder.questionOptionContent.isEnabled = false
        }



        // 리스너 초기화, 기존에 있던 recyclerview 체크 박스 다른 holder 충돌 방지
        holder.questionOptionContent.setOnCheckedChangeListener(null)


        // 단일 선택 = 미리 체크 되어있거나 초기값이 =-1 이라면
        if (curData.answerAt.equals("Y") && selectedPosition == -1) {
            // 선택 된 position 값을 현재 position 값으로 변경
            selectedPosition = holder.adapterPosition
        }


        // 다중선택 미리 체크 되어있을 때 response 값 true로 변경
        if (curData.answerAt.equals("Y")) {
            curData.isChecked = true
        }


        // 선택 상태 설정  각 아이템 갯수 만큼 지정
        // 아예 처음 일때
        holder.questionOptionContent.isChecked =
            if (surveyType.equals("N")) {

                holder.adapterPosition == selectedPosition
            } else {
                // 즉 다중일 때는 false로 지정 해서 박아 놓는 뜻
                curData.isChecked

            }


        //다중 선택
        if (surveyType.equals(("Y"))) {

            holder.questionOptionContent.setOnCheckedChangeListener { _, isChecked ->
                // 클릭 했을 때 true, 해제 하면 false 저장
                curData.isChecked = isChecked

                /*각 아이템들의 답변*/
                val requestGaekMulti = SurveyAnswerSaveQuestionRequestData(
                    curData.surveyQuestionSn.toString(),
                    curData.questionOptionSn.toString(),
                    etcAnswerContent = ""
                )

                if (curData.isChecked == true) {

                    /* 답변 저장할 때 필요한 데이터*/
                    SurveyInformation.allList.add(requestGaekMulti)
                }
                /* 체크가 풀렸다*/
                else {

                    SurveyInformation.allList.removeIf { it.surveyQuestionSn == curData.surveyQuestionSn && it.questionOptionSn == curData.questionOptionSn }
                }


            }

        }

        //단일선택
        else if (surveyType.equals("N")) {

            holder.questionOptionContent.setOnCheckedChangeListener { _, isChecked ->
                println(curData.questionOptionContent)

                if (isChecked) {

                    // 아예 젤 처음 들어가는게 -1로 지정
                    val prePosition = selectedPosition
                    selectedPosition = holder.adapterPosition

                    // 제일 처음과 제일 처음은 -1이니 바로 if 탈출 및  다음걸 누를 때 실행 되게
                    if (prePosition != -1) {

                        dataSet[prePosition].isChecked = false

                        //  전에꺼 여기서 change
                        notifyItemChanged(prePosition)
                    }
                    // 새로운 데이터 change
                    // 현재 선택 항목 업데이트
                    curData.isChecked = true
                    notifyItemChanged(selectedPosition)


                    /* 답변 저장할 때 필요한 데이터*/
                    val requestGaekOnlyOne = SurveyAnswerSaveQuestionRequestData(
                        curData.surveyQuestionSn.toString(),
                        curData.questionOptionSn.toString(),
                        etcAnswerContent = ""
                    )


                    /*리스트에서 답변 저장 */
                    // 기존 체크 된 데이터가 해당 번호들이 맞으면 삭제
                    SurveyInformation.allList.removeIf {
                        it.surveyQuestionSn == curData.surveyQuestionSn
                    }
                    //데이터 추가
                    SurveyInformation.allList.add(requestGaekOnlyOne)

                } else { // 단일 선택의 체크 해제 크게 신경 쓸 x
                    curData.isChecked = false

                    /*리스트에서 클릭했던 답변 삭제 */
                    SurveyInformation.allList.removeIf { it.surveyQuestionSn == curData.surveyQuestionSn && it.questionOptionSn == curData.questionOptionSn }
                }


            }

        }


    }
}
