package com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItemsData(

    @ColumnInfo(name = "name")
    val itemName: String,

    @ColumnInfo(name = "item_id")
    val itemId: Int,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "unit_price")
    val price: Int,

    @ColumnInfo(name = "order_id")
    val orderId: Int,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int
)