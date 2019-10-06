package com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses

import com.google.gson.annotations.SerializedName

data class OrderVendorPojo(

    @SerializedName("name")
    val vendorName: String,

    @SerializedName("id")
    val vendorId: Int
)