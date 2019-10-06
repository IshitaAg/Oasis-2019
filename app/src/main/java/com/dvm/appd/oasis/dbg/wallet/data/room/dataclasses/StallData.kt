package com.dvm.appd.oasis.dbg.wallet.data.room.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stalls")
data class StallData (
    @PrimaryKey
    val stallId:Int,

    val stallName:String,

    val closed:Boolean
)