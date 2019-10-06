package com.dvm.appd.oasis.dbg.elas.model.retrofit

import com.google.gson.annotations.SerializedName

data class Questions(

    @SerializedName("question_text")
    val question:String,

    @SerializedName("1")
    val option1:String,

    @SerializedName("2")
    val option2:String,

    @SerializedName("3")
    val option3:String,

    @SerializedName("4")
    val option4:String,

    @SerializedName("question_id")
    val qId:Long,

    val category:String
                     )