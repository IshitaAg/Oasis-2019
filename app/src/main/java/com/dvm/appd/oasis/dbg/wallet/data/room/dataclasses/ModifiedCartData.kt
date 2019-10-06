package com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses

data class ModifiedCartData(

    var itemId: Int,

    var itemName: String,

    var vendorId: Int,

    var vendorName: String,

    var quantity: Int,

    var price: Int,

    var isVeg: Boolean
)