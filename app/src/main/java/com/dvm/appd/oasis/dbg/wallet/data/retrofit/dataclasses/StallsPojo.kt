package com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses

import com.google.gson.annotations.SerializedName

data class StallsPojo(


    @SerializedName("name")
    val stallName: String,

    @SerializedName("id")
    val stallId: Int,

    val closed: Boolean,

    @SerializedName("menu")
    val items : List<StallItemsPojo>,

    @SerializedName("image_url")
    val imageUrl: String
)