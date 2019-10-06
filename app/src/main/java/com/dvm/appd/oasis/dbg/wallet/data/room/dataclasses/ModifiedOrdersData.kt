package com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses

data class ModifiedOrdersData(

    val orderId: Int,

    val shell: Int,

    val otp: Int,

    val otpSeen: Boolean,

    val status: Int,

    val totalPrice: Int,

    val vendor: String,

    val items: List<ModifiedItemsData>,

    val rating: Int
)