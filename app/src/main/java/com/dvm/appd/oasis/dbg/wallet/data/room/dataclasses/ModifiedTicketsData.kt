package com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses

data class ModifiedTicketsData(

    val ticketId: Int,

    val name: String,

    val price: Int,

    val type: String,

    val shows: String?,

    val quantity: Int,

    val cartId: Int
)