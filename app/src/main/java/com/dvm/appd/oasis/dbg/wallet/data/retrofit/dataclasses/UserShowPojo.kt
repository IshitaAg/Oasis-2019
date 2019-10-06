package com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses

import com.google.gson.annotations.SerializedName

data class UserShowPojo(

    @SerializedName("id")
    val id : Int,

    @SerializedName("show_name")
    val showName: String,

    @SerializedName("unused_count")
    val unusedCount: Int,

    @SerializedName("used_count")
    val usedCount: Int
)