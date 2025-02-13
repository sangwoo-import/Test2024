package com.example.mytest2024.swaggerapi

import com.example.mytest2024.swaggerapi.Retrofit.SurveyAnswerSaveQuestionRequestData

object SurveyInformation {
    var allList: MutableList<SurveyAnswerSaveQuestionRequestData> = mutableListOf()



    var surveySn: String? = null
    var multiCheckAt: String? = null

    fun saveSurveySn(surveySn: String?) {
        this.surveySn = surveySn
    }

    fun saveAllList(saveList: MutableList<SurveyAnswerSaveQuestionRequestData>) {
        this.allList = saveList
    }

    fun clearAllList() {
        this.allList.clear()
    }
}