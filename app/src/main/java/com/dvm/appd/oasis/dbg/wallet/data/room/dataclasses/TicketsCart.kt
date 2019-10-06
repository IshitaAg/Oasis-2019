package com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tickets_cart")
data class TicketsCart(

    @ColumnInfo(name = "ticket_id")
    val ticketId: Int,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int
)