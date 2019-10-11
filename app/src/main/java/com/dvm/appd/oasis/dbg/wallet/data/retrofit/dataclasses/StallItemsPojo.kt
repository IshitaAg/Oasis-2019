package com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses

import com.google.gson.annotations.SerializedName

data class StallItemsPojo(

    @SerializedName("id")
    val itemId: Int,

    @SerializedName("name")
    val itemName: String,

    @SerializedName("description")
    val category: String,

    @SerializedName("vendor_id")
    val stallId: Int,

    @SerializedName("is_veg")
    val isVeg: Boolean,

    @SerializedName("is_available")
    val isAvailable: Boolean,

    @SerializedName("price")
    val currentPrice: Int,

    @SerializedName("current_discount")
    val discount: Int,

    @SerializedName("base_price")
    val basePrice: Int
)