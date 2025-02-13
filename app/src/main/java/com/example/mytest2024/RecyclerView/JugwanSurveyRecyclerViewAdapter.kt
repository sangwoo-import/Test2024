import com.example.mytest2024.swaggerapi.Retrofit.SurveyOptionListResponse

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.mytest2024.R
import com.example.mytest2024.swaggerapi.Retrofit.SurveyAnswerSaveQuestionRequestData
import com.example.mytest2024.swaggerapi.SurveyInformation

// view : item_list 를 의미
// 수정 하기 위한 이미지뷰 , text 가져옴 , 홀더 = 객체 저장 용도
class JugwanSurveyCustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var surveyJugwanContent = view.findViewById<EditText>(R.id.surveyJugwanContent)


}


class JugwanSurveyRecyclerViewAdapter(
    // 원래는 List
    var dataSet: List<SurveyOptionListResponse>,
    val context: Context,
    var surveyType: String,
    var answerAt : String

) : RecyclerView.Adapter<JugwanSurveyCustomViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): JugwanSurveyCustomViewHolder {
        val callForRow =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.survey_jugwan_item_list, parent, false)
        return JugwanSurveyCustomViewHolder(callForRow)
    }

    override fun getItemCount() = dataSet.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: JugwanSurveyCustomViewHolder, position: Int) {

        var curData = dataSet[position]

        /*답변이 완료 된거면 EditText 비활성화*/

        if(answerAt.equals("Y")){
            holder.surveyJugwanContent.isEnabled = false
        }


        if(!curData.etcAnswerContent.isNullOrEmpty()) {
            holder.surveyJugwanContent.setText(curData.etcAnswerContent)
        }else{
            holder.surveyJugwanContent.hint = "답변이 없어요"

        }



        // 텍스트 변경 리스너 추가
                holder.surveyJugwanContent.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                    override fun afterTextChanged(s: Editable?) {
                        // 입력된 텍스트로 SurveyAnswerSaveQuestionRequestData 생성
                        val requestData = SurveyAnswerSaveQuestionRequestData(
                            surveyQuestionSn = curData.surveyQuestionSn.toString(),
                            questionOptionSn = curData.questionOptionSn.toString(),
                            etcAnswerContent = s.toString()
                        )

                        // 기존 항목 제거 후 새 항목 추가
                        SurveyInformation.allList?.removeAll {
                            it.surveyQuestionSn == curData.surveyQuestionSn.toString() &&
                                    it.questionOptionSn == curData.questionOptionSn.toString()
                        }
                        SurveyInformation.allList?.add(requestData)
                    }
                })

    }
}