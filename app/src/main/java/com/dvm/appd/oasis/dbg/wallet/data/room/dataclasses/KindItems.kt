package com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import javax.annotation.Nullable

@Entity(tableName = "kindItems")
data class KindItems(

    @PrimaryKey
    val id :Int,

    @ColumnInfo(name = "itemName")
    val name:String,

    @ColumnInfo(name = "price")
    val price:Int,

    @ColumnInfo(name = "availability")
    val isAvailable:Boolean,

    @ColumnInfo(name="itemImage")
    val image:String?
)