package com.example.mytest2024.SwaggerAPI

import com.example.mytest2024.SwaggerAPI.Retrofit.SurveyAnswerSaveQuestionRequestData

object SurveyInformation {

    var surveySn: String? = null
    var multiCheckAt: String? = null
    var allList: MutableList<SurveyAnswerSaveQuestionRequestData> = mutableListOf()

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