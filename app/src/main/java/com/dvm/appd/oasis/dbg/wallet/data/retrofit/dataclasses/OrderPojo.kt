package com.dvm.appd.oasis.dbg.wallet.data.retrofit.dataclasses

import com.google.gson.annotations.SerializedName

data class OrderPojo(

    @SerializedName("transaction")
    val transaction: String?,

    @SerializedName("order-id")
    val id: Int,

    @SerializedName("items")
    val items: List<OrderItemsPojo>,

    @SerializedName("vendor")
    val vendor: OrderVendorPojo,

    @SerializedName("shell")
    val shell: Int,

    @SerializedName("otp_seen")
    val otpSeen: Boolean,

    @SerializedName("otp")
    val otp: Int,

    @SerializedName("status")
    val status: Int,

    @SerializedName("price")
    val price: Int,

    @SerializedName("rating")
    val rating: Int
)