package com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses

import com.google.gson.annotations.SerializedName

data class OrderItemsPojo(

    @SerializedName("name")
    val itemName: String,

    @SerializedName("id")
    val itemId: Int,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("unit_price")
    val price: Int
)