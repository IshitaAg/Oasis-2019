package com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_tickets")
data class UserShows(

    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "show")
    val name: String,

    @ColumnInfo(name = "used")
    val used: Int,

    @ColumnInfo(name = "unused")
    val unused: Int
)