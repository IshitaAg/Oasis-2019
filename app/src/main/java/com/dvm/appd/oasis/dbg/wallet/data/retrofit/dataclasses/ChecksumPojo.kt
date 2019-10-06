package com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses

import com.google.gson.annotations.SerializedName

data class ChecksumPojo(

    @SerializedName("CHECKSUMHASH")
    val checksumHash: String,

    @SerializedName("ORDER_ID")
    val orderId: String,

    @SerializedName("payt_STATUS")
    val paytStatus: String
)