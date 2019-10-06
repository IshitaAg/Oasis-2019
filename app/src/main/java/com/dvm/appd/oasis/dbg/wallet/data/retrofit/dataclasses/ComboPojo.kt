package com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses

import com.google.gson.annotations.SerializedName

data class ComboPojo(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("price")
    val price: Int,

    @SerializedName("shows")
    val shows: List<ComboShowPojo>
)