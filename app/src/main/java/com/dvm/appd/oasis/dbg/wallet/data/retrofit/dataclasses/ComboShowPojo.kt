package com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses

import com.google.gson.annotations.SerializedName

data class ComboShowPojo(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String
)