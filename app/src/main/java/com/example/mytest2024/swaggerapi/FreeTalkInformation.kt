package com.example.mytest2024.swaggerapi

object FreeTalkInformation {


    var boardId: String? = null
    var postingSn: Int? = null

    fun saveFreeTalkInfos(boardId: String?, postingSn: Int?) {
        this.boardId = boardId
        this.postingSn = postingSn
    }


}