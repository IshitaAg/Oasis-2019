package com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses

data class ChildOrdersData(

    val orderId: Int,

    val shell: Int,

    val otp: Int,

    val otpSeen: Boolean,

    val status: Int,

    val totalPrice: Int,

    val vendor: String,

    val itemName: String,

    val itemId: Int,

    val quantity: Int,

    val price: Int,

    val rating: Int
)