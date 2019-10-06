package com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses

import com.google.gson.annotations.SerializedName

data class PaytmPojo(

    @SerializedName("MID")
    val mID: String,

    @SerializedName("ORDER_ID")
    val orderId: String,

    @SerializedName("CUST_ID")
    val custId: String,

    @SerializedName("CHANNEL_ID")
    val channelId: String,

    @SerializedName("TXM_AMOUNT")
    val txnAmount: String,

    @SerializedName("WEBSITE")
    val website : String,

    @SerializedName("CALLBACK_URL")
    val callBackUrl: String,

    @SerializedName("INDUSTRY_TYPE_ID")
    val industryTypeId: String
)