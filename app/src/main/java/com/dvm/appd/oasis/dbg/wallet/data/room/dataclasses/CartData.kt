package com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_data")
data class CartData(

    @PrimaryKey
    @ColumnInfo(name = "item_id")
    var itemId: Int,

    @ColumnInfo(name = "quantity")
    var quantity: Int,

    @ColumnInfo(name = "vendor_id")
    var vendorId: Int
)