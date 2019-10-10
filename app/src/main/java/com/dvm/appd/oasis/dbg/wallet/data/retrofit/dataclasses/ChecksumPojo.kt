package com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses

import com.google.gson.annotations.SerializedName

data class ChecksumPojo(

    @SerializedName("CHECKSUMHASH")
    val checksumHash: String,

    @SerializedName("ORDER_ID")
    val orderId: String,

    @SerializedName("CUST_ID")
    val customerId: String,

    @SerializedName("MID")
    val mid: String,

    @SerializedName("CHANNEL_ID")
    val channelId: String,

    @SerializedName("TXN_AMOUNT")
    val amount: String,

    @SerializedName("WEBSITE")
    val website: String,

    @SerializedName("CALLBACK_URL")
    val callBackUrl: String,

    @SerializedName("INDUSTRY_TYPE_ID")
    val industryTypeId: String

    /*@SerializedName("payt_STATUS")
    val paytStatus: String*/
)