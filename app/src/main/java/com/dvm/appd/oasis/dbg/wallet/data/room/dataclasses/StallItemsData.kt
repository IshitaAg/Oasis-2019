package com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stall_items")
data class StallItemsData(

    @PrimaryKey
    @ColumnInfo(name = "itemId")
    val itemId:Int,

    @ColumnInfo(name ="itemName")
    val itemName:String,

    @ColumnInfo(name = "stallId")
    val stallId:Int,

    @ColumnInfo(name = "category")
    val category: String,

    @ColumnInfo(name = "current_price")
    val currentPrice:Int,

    @ColumnInfo(name = "isAvailable")
    val isAvailable:Boolean,

    @ColumnInfo(name = "isVeg")
    val isVeg: Boolean,

    @ColumnInfo(name = "discount")
    val discount: Int,

    @ColumnInfo(name = "base_price")
    val basePrice: Int

    )