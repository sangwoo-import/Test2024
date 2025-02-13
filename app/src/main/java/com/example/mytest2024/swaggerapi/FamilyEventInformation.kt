package com.example.mytest2024.swaggerapi

object FamilyEventInformation {

    var boardId: String? = null
    var postingSn: Int? = null

    fun saveFamilyEventInfos(boardId: String?, postingSn: Int?) {
        this.boardId = boardId
        this.postingSn = postingSn
    }
}