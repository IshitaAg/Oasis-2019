package com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses

data class ModifiedStallItemsData(

    val itemId:Int,

    val itemName:String,

    val stallId:Int,

    val category: String,

    val currentPrice:Int,

    val quantity: Int,

    val isVeg: Boolean,

    val discount: Int,

    val basePrice: Int
)