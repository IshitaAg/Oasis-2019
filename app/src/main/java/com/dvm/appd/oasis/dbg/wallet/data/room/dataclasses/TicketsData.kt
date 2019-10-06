package com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nullable

@Entity(tableName = "tickets")
data class TicketsData(

    @ColumnInfo(name = "ticket_id")
    val ticketId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "price")
    val price: Int,

    @ColumnInfo(name = "type")
    val type: String,

    @ColumnInfo(name = "shows")
    @Nullable
    val shows: String?,

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Int
)